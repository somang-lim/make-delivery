package com.sm.makedelivery.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sm.makedelivery.annotation.LoginCheck;
import com.sm.makedelivery.annotation.LoginCheck.UserLevel;
import com.sm.makedelivery.dto.OrderReceiptDTO;
import com.sm.makedelivery.service.DeliveryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/orders", params = "status")
@RequiredArgsConstructor
public class DeliveryController {

	private final DeliveryService deliveryService;


	@GetMapping(params = "orderId")
	@LoginCheck(userLevel = UserLevel.RIDER)
	public ResponseEntity<OrderReceiptDTO> loadStandbyOrder(long orderId, String riderAddress) {
		OrderReceiptDTO orderReceipt = deliveryService.loadStandbyOrder(orderId, riderAddress);

		return ResponseEntity.ok(orderReceipt);
	}

	@GetMapping
	@LoginCheck(userLevel = UserLevel.RIDER)
	public ResponseEntity<List<String>> loadStandbyOrderList(String riderAddress) {
		List<String> orderList = deliveryService.loadStandbyOrderList(riderAddress);

		return ResponseEntity.ok(orderList);
	}


}
