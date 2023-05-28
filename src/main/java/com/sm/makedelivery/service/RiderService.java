package com.sm.makedelivery.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.firebase.messaging.Message;
import com.sm.makedelivery.dao.DeliveryDAO;
import com.sm.makedelivery.dto.OrderDTO.OrderStatus;
import com.sm.makedelivery.dto.PushMessageDTO;
import com.sm.makedelivery.dto.RiderDTO;
import com.sm.makedelivery.dto.UserInfoDTO;
import com.sm.makedelivery.mapper.OrderMapper;
import com.sm.makedelivery.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RiderService {

	private final DeliveryDAO deliveryDAO;
	private final OrderMapper orderMapper;
	private final UserMapper userMapper;
	private final PushService pushService;


	public void registerStandbyRider(String riderId) throws IOException {
		RiderDTO rider = setStartStandbyRider(riderId);

		deliveryDAO.insertStandbyRiderWhenStartWork(rider);
	}

	private RiderDTO setStartStandbyRider(String riderId) throws IOException {
		UserInfoDTO userInfo = userMapper.selectUserInfo(riderId);

		RiderDTO rider = RiderDTO.builder()
			.id(riderId)
			.name(userInfo.getName())
			.phone(userInfo.getPhone())
			.address(userInfo.getAddress())
			.fcmToken(getFcmToken())
			.build();

		return rider;
	}

	private String getFcmToken() throws IOException {
		return pushService.getAccessToken();
	}

	public void deleteStandbyRiderWhenStopWork(String riderId) {
		RiderDTO rider = getStandbyRider(riderId);

		deliveryDAO.deleteStandbyRiderWhenStopWork(rider);
	}

	private RiderDTO getStandbyRider(String riderId) {
		UserInfoDTO userInfo = userMapper.selectUserInfo(riderId);

		RiderDTO rider = RiderDTO.builder()
			.id(riderId)
			.name(userInfo.getName())
			.phone(userInfo.getPhone())
			.address(userInfo.getAddress())
			.build();

		return rider;
	}

	@Transactional
	public void acceptStandbyOrder(long orderId, String riderId) {
		RiderDTO rider = getStandbyRider(riderId);

		deliveryDAO.updateStandbyOrderToDelivering(orderId, rider);
		orderMapper.updateStandbyOrderToDelivering(orderId, riderId, OrderStatus.DELIVERING);

	}

	@Transactional
	public void finishDeliveringOrder(long orderId, String riderId) throws IOException {
		RiderDTO rider = setStartStandbyRider(riderId);

		orderMapper.finishDeliveringOrder(orderId, OrderStatus.COMPLETE_DELIVERY);
		deliveryDAO.insertStandbyRiderWhenStartWork(rider);
	}

	public void sendMessageToStandbyRidersInSameArea(String address, PushMessageDTO pushMessage) {

		Set<String> tokenSet = deliveryDAO.selectStandbyRiderTokenList(address);

		List<Message> messages = tokenSet.stream().map(token -> Message.builder()
			.putData("title", pushMessage.getTitle())
			.putData("content", pushMessage.getContent())
			.putData("orderRecipt", pushMessage.getOrderReceipt().toString())
			.putData("createdAt", pushMessage.getCreatedAt())
			.setToken(token)
			.build())
			.collect(Collectors.toList());

		pushService.sendMessages(messages);
	}

}
