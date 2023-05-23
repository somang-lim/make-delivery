package com.sm.makedelivery.dao;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.sm.makedelivery.dto.CartItemDTO;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CartItemDAO {

	private static final String cartKey = ":CART";
	private final RedisTemplate<String, CartItemDTO> redisTemplate;


	public List<CartItemDTO> selectCartList(String userId) {
		final String key = generateCartKey(userId);

		List<CartItemDTO> cartList = redisTemplate
			.opsForList()
			.range(key, 0, -1);

		return cartList;
	}

	public static String generateCartKey(String id) {
		return id + cartKey;
	}

	public void insertMenu(String userId, CartItemDTO cart) {
		final String key = generateCartKey(userId);

		redisTemplate.opsForList().rightPush(key, cart);
	}

	public void deleteMenuList(String userId) {
		final String key = generateCartKey(userId);

		redisTemplate.delete(key);
	}

}
