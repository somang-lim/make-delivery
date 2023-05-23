package com.sm.makedelivery.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sm.makedelivery.dao.CartItemDAO;
import com.sm.makedelivery.dto.CartItemDTO;
import com.sm.makedelivery.dto.CartOptionDTO;
import com.sm.makedelivery.dto.OrderDTO;
import com.sm.makedelivery.dto.OrderDTO.OrderStatus;
import com.sm.makedelivery.dto.OrderMenuDTO;
import com.sm.makedelivery.dto.OrderMenuOptionDTO;
import com.sm.makedelivery.dto.UserInfoDTO;
import com.sm.makedelivery.mapper.OrderMapper;
import com.sm.makedelivery.mapper.OrderMenuMapper;
import com.sm.makedelivery.mapper.OrderMenuOptionMapper;
import com.sm.makedelivery.mapper.UserMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OrderService {

	private final OrderMapper orderMapper;
	private final OrderMenuMapper orderMenuMapper;
	private final OrderMenuOptionMapper orderMenuOptionMapper;
	private final UserMapper userMapper;
	private final CartItemDAO cartItemDAO;


	@Transactional
	public void registerOrder(String userId, long storeId) {
		UserInfoDTO userInfo = userMapper.selectUserInfo(userId);
		OrderDTO order = getOrder(userInfo, storeId);
		orderMapper.insertOrder(order);

		List<CartItemDTO> cartList = cartItemDAO.selectCartList(userId);
		registerOrderMenu(cartList, order.getId());
	}

	private OrderDTO getOrder(UserInfoDTO userInfo, long storeId) {
		OrderDTO order = OrderDTO.builder()
			.address(userInfo.getAddress())
			.userId(userInfo.getId())
			.orderStatus(OrderStatus.BEFORE_ORDER)
			.storeId(storeId)
			.build();

		return order;
	}

	private void registerOrderMenu(List<CartItemDTO> cartList, long orderId) {
		List<OrderMenuDTO> orderMenuList = new ArrayList<>();
		List<OrderMenuOptionDTO> orderMenuOptionList = new ArrayList<>();

		long totalPrice = 0;

		for (CartItemDTO item : cartList) {
			totalPrice += item.getPrice() * item.getCount();

			OrderMenuDTO orderMenu = OrderMenuDTO.builder()
				.orderId(orderId)
				.menuId(item.getMenuId())
				.count(item.getCount())
				.build();

			orderMenuList.add(orderMenu);

			for (CartOptionDTO option : item.getOptionList()) {
				totalPrice += option.getPrice();

				OrderMenuOptionDTO orderMenuOption = OrderMenuOptionDTO.builder()
					.optionId(option.getOptionId())
					.menuId(item.getMenuId())
					.orderId(orderId)
					.build();

				orderMenuOptionList.add(orderMenuOption);
			}
		}

		orderMenuMapper.insertOrderMenu(orderMenuList);
		orderMenuOptionMapper.insertOrderMenuOption(orderMenuOptionList);
		orderMapper.updateTotalPrice(totalPrice, orderId);
	}

}
