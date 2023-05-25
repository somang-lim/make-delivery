package com.sm.makedelivery.service;

import static com.sm.makedelivery.dto.PayDTO.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.sm.makedelivery.dao.CartItemDAO;
import com.sm.makedelivery.dto.CartItemDTO;
import com.sm.makedelivery.dto.OrderDTO;
import com.sm.makedelivery.dto.OrderDTO.OrderStatus;
import com.sm.makedelivery.dto.OrderDetailDTO;
import com.sm.makedelivery.dto.OrderMenuDTO;
import com.sm.makedelivery.dto.OrderMenuOptionDTO;
import com.sm.makedelivery.dto.OrderReceiptDTO;
import com.sm.makedelivery.dto.OrderStoreDetailDTO;
import com.sm.makedelivery.dto.PayDTO;
import com.sm.makedelivery.dto.StoreInfoDTO;
import com.sm.makedelivery.dto.UserInfoDTO;
import com.sm.makedelivery.mapper.OrderMapper;
import com.sm.makedelivery.mapper.StoreMapper;
import com.sm.makedelivery.mapper.UserMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OrderService {

	private final OrderMapper orderMapper;
	private final UserMapper userMapper;
	private final StoreMapper storeMapper;
	private final OrderTransactionService orderTransactionService;
	private final CartItemDAO cartItemDAO;


	@Transactional
	public OrderReceiptDTO registerOrder(String userId, long storeId, PayType payType) {
		UserInfoDTO user = userMapper.selectUserInfo(userId);
		OrderDTO order = getOrder(user, storeId);

		List<CartItemDTO> cartList;
		List<OrderMenuDTO> orderMenuList = new ArrayList<>();
		List<OrderMenuOptionDTO> orderMenuOptionList = new ArrayList<>();
		OrderReceiptDTO orderReceipt;

		cartList = cartItemDAO.getCartAndDelete(userId);

		restoreCartListOnOrderRollback(userId, cartList);

		long totalPrice = orderTransactionService.order(order, cartList, orderMenuList, orderMenuOptionList);

		orderTransactionService.pay(payType, totalPrice, order.getId());
		orderMapper.completeOrder(totalPrice, order.getId(), OrderStatus.COMPLETE_ORDER);
		OrderDTO completeOrder = orderMapper.selectOrder(order.getId());

		orderReceipt = getOrderReceipt(completeOrder, cartList, totalPrice, storeId, user);

		return orderReceipt;
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

	private void restoreCartListOnOrderRollback(String userId, List<CartItemDTO> cartList) {
		TransactionSynchronizationManager.registerSynchronization(
			new TransactionSynchronization() {
				@Override
				public void afterCompletion(int status) {
					if (status == STATUS_ROLLED_BACK) {
						cartItemDAO.insertMenuList(userId, cartList);
					}
				}
			}
		);
	}

	private OrderReceiptDTO getOrderReceipt(OrderDTO order, List<CartItemDTO> cartList, long totalPrice, long storeId, UserInfoDTO userInfo) {
		StoreInfoDTO storeInfo = storeMapper.selectStoreInfo(storeId);

		return OrderReceiptDTO.builder()
			.orderId(order.getId())
			.orderStatus(OrderStatus.COMPLETE_ORDER.toString())
			.userInfo(userInfo)
			.totalPrice(totalPrice)
			.storeInfo(storeInfo)
			.cartList(cartList)
			.build();
	}

	public OrderReceiptDTO loadOrder(long orderId) {
		OrderReceiptDTO orderReceipt = orderMapper.selectOrderReceipt(orderId);

		return orderReceipt;
	}

	public List<OrderStoreDetailDTO> loadStoreOrder(long storeId) {
		List<OrderStoreDetailDTO> orderStoreDetailList = orderMapper.selectDetailStoreOrder(storeId);

		return orderStoreDetailList;
	}

}
