package com.sm.makedelivery.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Repository;

import com.sm.makedelivery.dto.CartItemDTO;

import lombok.RequiredArgsConstructor;

/*
	Redis의 트랜잭션은 RDB의 트랜잭션과 다릅니다. Redis의 트랜잭션은 rollback 기능이 없습니다.
	Redis 트랜잭션에서 잘못된 명령어나 데이터타입을 실수로 쓸 때만 오류가 발생하기 때문에,
	전부 다 rollback 하지 않고 exec 후 오류가 나지 않은 부분은 실행됩니다.
	exec 이전에 command queue에 적재하는 도중 실패하는 경우 (command 문법 오류, 메모리 부족 오류,
	다른 클라이언트에서 command를 날려 atomic이 보장되지 않는 경우) 에는 exec하면 전부 discard 됩니다.

	* 실험해보니 multi 후 트랜잭션 중 다른 스레드에서 command를 날리면 discard 됩니다.
	* Redis 2.6.5 이후로 트랜잭션 시작 후 오류가 있으면 exec 될 대 전부 discard 됩니다.

	트랜잭션 명령어들은 exec 되기 위해 queue에서 기다리는데 discard를 이용해서 실행하지 않을 수 있습니다.
	트랜잭션의 locking은 watch를 이용해 optimistic locking(낙관적 락)입니다.
	watch로 어떠한 키를 감시하고 이 키의 트랜잭션이 multi로 시작되기 전에
	watch할 때의 값과 multi할 때의 값이 변경되지 않아야 트랜잭션이 시작할 수 있습니다.
	만약 이 값이 변경 되었다면 RAce condition이 일어난 것이기 때문에 트랜잭션에 에러가 발생합니다.
 */

@Repository
@RequiredArgsConstructor
public class CartItemDAO {

	private final RedisTemplate<String, CartItemDTO> redisTemplate;
	private static final String cartKey = ":CART";


	public static String generateCartKey(String id) {
		return id + cartKey;
	}

	public List<CartItemDTO> selectCartList(String userId) {
		final String key = generateCartKey(userId);

		List<CartItemDTO> cartList = redisTemplate
			.opsForList()
			.range(key, 0, -1);

		return cartList;
	}

	public void insertMenu(String userId, CartItemDTO cart) {
		final String key = generateCartKey(userId);

		redisTemplate.opsForList().rightPush(key, cart);
	}

	public void deleteMenuList(String userId) {
		final String key = generateCartKey(userId);

		redisTemplate.delete(key);
	}

	public List<CartItemDTO> getCartAndDelete(String userId) {
		final String key = generateCartKey(userId);

		List<Object> cartListObject = redisTemplate.execute(
			new SessionCallback<>() {
				@Override
				public List<Object> execute(RedisOperations redisOperations) throws DataAccessException {
					try {
						redisOperations.watch(key);
						redisOperations.multi();
						redisOperations.opsForList().range(key, 0, -1);
						redisOperations.delete(key);

						return redisOperations.exec();
					} catch (Exception e) {
						redisOperations.discard();

						throw e;
					}
				}
			}
		);

		List<CartItemDTO> cartList = (List<CartItemDTO>) cartListObject.get(0);

		return cartList;
	}

	public void insertMenuList(String userId, List<CartItemDTO> cartList) {
		final String key = generateCartKey(userId);

		RedisSerializer keySerializer = redisTemplate.getStringSerializer();
		RedisSerializer valueSerializer = redisTemplate.getValueSerializer();

		redisTemplate.executePipelined( (RedisCallback<Object>) RedisConnection -> {
			cartList.forEach(cart -> {
				RedisConnection.rPush(keySerializer.serialize(key), valueSerializer.serialize(cart));
			});

			return null;
		});
	}

}
