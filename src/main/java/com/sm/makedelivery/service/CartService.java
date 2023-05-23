package com.sm.makedelivery.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sm.makedelivery.dao.CartItemDAO;
import com.sm.makedelivery.dto.CartItemDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

	private final CartItemDAO cartItemDAO;


	public List<CartItemDTO> loadCart(String userId) {
		return cartItemDAO.selectCartList(userId);
	}

	public void registerMenuInCart(String userId, CartItemDTO cart) {
		cartItemDAO.insertMenu(userId, cart);
	}

	public void deleteAllMenuInCart(String userId) {
		cartItemDAO.deleteMenuList(userId);
	}

}
