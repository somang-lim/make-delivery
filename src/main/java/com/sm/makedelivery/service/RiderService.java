package com.sm.makedelivery.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sm.makedelivery.dao.DeliveryDAO;
import com.sm.makedelivery.dto.OrderDTO.OrderStatus;
import com.sm.makedelivery.dto.RiderDTO;
import com.sm.makedelivery.mapper.OrderMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RiderService {

	private final DeliveryDAO deliveryDAO;
	private final OrderMapper orderMapper;


	public void registerStandbyRider(RiderDTO rider) {
		deliveryDAO.insertStandbyRiderWhenStartWork(rider);
	}

	public void deleteStandbyRiderWhenStopWork(RiderDTO rider) {
		deliveryDAO.deleteStandbyRiderWhenStopWork(rider);
	}

	@Transactional
	public void acceptStandbyOrder(long orderId, RiderDTO rider) {
		deliveryDAO.updateStandbyOrderToDelivering(orderId, rider);
		orderMapper.updateStandbyOrderToDelivering(orderId, rider.getId(), OrderStatus.DELIVERING);

	}

	@Transactional
	public void finishDeliveringOrder(long orderId, RiderDTO rider) {
		orderMapper.finishDeliveringOrder(orderId, OrderStatus.COMPLETE_DELIVERY);
		deliveryDAO.insertStandbyRiderWhenStartWork(rider);
	}
}
