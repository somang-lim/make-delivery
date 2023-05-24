package com.sm.makedelivery.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sm.makedelivery.annotation.CurrentUserId;
import com.sm.makedelivery.annotation.LoginCheck;
import com.sm.makedelivery.annotation.LoginCheck.UserLevel;
import com.sm.makedelivery.dto.CartItemDTO;
import com.sm.makedelivery.service.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users/carts")
@RequiredArgsConstructor
public class CartController {

	private final CartService cartService;


	@PostMapping
	@LoginCheck(userLevel = UserLevel.USER)
	public void registerMenuInCart(@CurrentUserId String userId, @Valid @RequestBody CartItemDTO cart) {
		cartService.registerMenuInCart(userId, cart);
	}

	@GetMapping
	@LoginCheck(userLevel = UserLevel.USER)
	public ResponseEntity<List<CartItemDTO>> loadCart(@CurrentUserId String userId) {
		List<CartItemDTO> cartList = cartService.loadCart(userId);

		return ResponseEntity.ok(cartList);
	}

	@DeleteMapping
	@LoginCheck(userLevel = UserLevel.USER)
	public void deleteAllMenuInCart(@CurrentUserId String userId) {
		cartService.deleteAllMenuInCart(userId);
	}

}
