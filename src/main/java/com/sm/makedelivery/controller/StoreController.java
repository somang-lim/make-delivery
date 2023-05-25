package com.sm.makedelivery.controller;

import static com.sm.makedelivery.annotation.LoginCheck.*;
import static com.sm.makedelivery.utils.ResponseEntityConstants.RESPONSE_OK;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sm.makedelivery.annotation.CurrentUserId;
import com.sm.makedelivery.annotation.LoginCheck;
import com.sm.makedelivery.dto.StoreDTO;
import com.sm.makedelivery.service.StoreService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

	private final StoreService storeService;


	@PostMapping
	@LoginCheck(userLevel = UserLevel.OWNER)
	public ResponseEntity<Void> insertStore(StoreDTO store, @CurrentUserId String ownerId) {
		storeService.insertStore(store, ownerId);

		return RESPONSE_OK;
	}

	@GetMapping
	@LoginCheck(userLevel = UserLevel.OWNER)
	public ResponseEntity<List<StoreDTO>> getMyAllStore(@CurrentUserId String ownerId) {
		List<StoreDTO> stores = storeService.getMyAllStore(ownerId);

		return ResponseEntity.ok(stores);
	}

	@GetMapping("/{storeId}")
	@LoginCheck(userLevel = UserLevel.OWNER)
	public ResponseEntity<StoreDTO> getMyStore(@PathVariable long storeId, @CurrentUserId String ownerId) {
		storeService.validateMyStore(storeId, ownerId);
		StoreDTO store = storeService.getMyStore(storeId, ownerId);

		return ResponseEntity.ok(store);
	}

	@PatchMapping("/{storeId}/closed")
	@LoginCheck(userLevel = UserLevel.OWNER)
	public ResponseEntity<Void> closeMyStore(@PathVariable long storeId, @CurrentUserId String ownerId) {
		storeService.validateMyStore(storeId, ownerId);
		storeService.closeMyStore(storeId);

		return RESPONSE_OK;
	}

	@PatchMapping("/{storeId}/opened")
	@LoginCheck(userLevel = UserLevel.OWNER)
	public ResponseEntity<Void> openMyStore(@PathVariable long storeId, @CurrentUserId String ownerId) {
		storeService.validateMyStore(storeId, ownerId);
		storeService.openMyStore(storeId);

		return RESPONSE_OK;
	}

	@PostMapping("/{storeId}/orders/{orderId}/approve")
	@LoginCheck(userLevel = UserLevel.OWNER)
	public void approveOrder(@PathVariable long orderId, @PathVariable long storeId, @CurrentUserId String ownerId) {
		storeService.validateMyStore(storeId, ownerId);
		storeService.approveOrder(orderId);
	}

}
