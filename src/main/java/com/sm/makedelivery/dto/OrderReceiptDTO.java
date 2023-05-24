package com.sm.makedelivery.dto;

import java.util.List;

import com.sm.makedelivery.dto.OrderDTO.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class OrderReceiptDTO {

	private final Long orderId;

	private final OrderStatus orderStatus;

	private final UserInfoDTO userInfo;

	private final Long totalPrice;

	private final StoreInfoDTO storeInfo;

	private final List<CartItemDTO> cartList;

}
