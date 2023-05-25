package com.sm.makedelivery.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class OrderDTO {

	private final Long id;

	private final LocalDateTime createdAt;

	private final OrderStatus orderStatus;

	private final String address;

	private final String userId;

	private final Long storeId;

	private final Long totalPrice;


	public enum OrderStatus {
		BEFORE_ORDER, COMPLETE_ORDER, APPROVED_ORDER, DELIVERING, COMPLETE_DELIVERY

	}

}
