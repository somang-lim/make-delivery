package com.sm.makedelivery.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sm.makedelivery.annotation.CurrentUserId;
import com.sm.makedelivery.annotation.LoginCheck;
import com.sm.makedelivery.annotation.LoginCheck.UserLevel;
import com.sm.makedelivery.dto.OrderDetailDTO;
import com.sm.makedelivery.dto.OrderReceiptDTO;
import com.sm.makedelivery.dto.OrderStoreDetailDTO;
import com.sm.makedelivery.dto.PayDTO;
import com.sm.makedelivery.dto.PayDTO.PayType;
import com.sm.makedelivery.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/stores/{storeId}/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;


	@PostMapping
	@LoginCheck(userLevel = UserLevel.USER)
	public ResponseEntity<OrderReceiptDTO> registerOrder(@CurrentUserId String userId, @PathVariable long storeId, PayType payType) {
		OrderReceiptDTO orderReceipt = orderService.registerOrder(userId, storeId, payType);

		return ResponseEntity.ok(orderReceipt);
	}

	@GetMapping("/{orderId}")
	@LoginCheck(userLevel = UserLevel.USER)
	public ResponseEntity<OrderDetailDTO> loadOrder(@PathVariable long orderId) {
		OrderDetailDTO orderReceipt = orderService.loadOrder(orderId);

		return ResponseEntity.ok(orderReceipt);
	}

	@GetMapping
	@LoginCheck(userLevel = UserLevel.OWNER)
	public ResponseEntity<List<OrderStoreDetailDTO>> loadStoreOrder(@PathVariable long storeId) {
		List<OrderStoreDetailDTO> orderList = orderService.loadStoreOrder(storeId);

		return ResponseEntity.ok(orderList);
	}

}
