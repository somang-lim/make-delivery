package com.sm.makedelivery.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sm.makedelivery.annotation.CurrentUserId;
import com.sm.makedelivery.annotation.LoginCheck;
import com.sm.makedelivery.annotation.LoginCheck.UserLevel;
import com.sm.makedelivery.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/stores/{storeId}/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;


	@PostMapping
	@LoginCheck(userLevel = UserLevel.USER)
	public void registerOrder(@CurrentUserId String userId, @PathVariable long storeId) {
		orderService.registerOrder(userId, storeId);
	}

}
