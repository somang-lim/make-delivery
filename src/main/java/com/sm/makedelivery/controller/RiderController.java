package com.sm.makedelivery.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sm.makedelivery.annotation.LoginCheck;
import com.sm.makedelivery.annotation.LoginCheck.UserLevel;
import com.sm.makedelivery.dto.RiderDTO;
import com.sm.makedelivery.service.RiderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/riders/{riderId}")
@RequiredArgsConstructor
public class RiderController {

	private final RiderService riderService;


	@PostMapping("/standby")
	@LoginCheck(userLevel = UserLevel.RIDER)
	public void registerStandbyRider(@PathVariable String riderId) throws IOException {
		riderService.registerStandbyRider(riderId);
	}

	@DeleteMapping("/standby")
	@LoginCheck(userLevel = UserLevel.RIDER)
	public void deleteStandbyRider(@PathVariable String riderId) {
		riderService.deleteStandbyRiderWhenStopWork(riderId);
	}

	@PatchMapping("/orders/{orderId}/accept")
	@LoginCheck(userLevel = UserLevel.RIDER)
	public void acceptStandbyOrder(@PathVariable long orderId, @PathVariable String riderId) {
		riderService.acceptStandbyOrder(orderId, riderId);
	}

	@PatchMapping("/orders/{orderId}/finish")
	@LoginCheck(userLevel = UserLevel.RIDER)
	public void finishDeliveringOrder(@PathVariable long orderId, RiderDTO rider) {
		riderService.finishDeliveringOrder(orderId, rider);
	}

}
