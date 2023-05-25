package com.sm.makedelivery.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Repository;

import com.sm.makedelivery.dto.OrderReceiptDTO;
import com.sm.makedelivery.dto.RiderDTO;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DeliveryDAO {

	@Qualifier("deliveryRedisTemplate")
	private final RedisTemplate<String, RiderDTO> redisTemplate;
	private static final String STANDBY_ORDERS_KEY = ":STANDBY_ORDERS";
	private static final String STANDBY_RIDERS_KEY = ":STANDBY_RIDERS";


	private static String generateOrderHashKey(long orderId) {
		return String.valueOf(orderId);
	}

	private static String generateStandbyOrderKey(String address) {
		return address + STANDBY_ORDERS_KEY;
	}

	private static String generateStandbyRiderKey(String address) {
		return address + STANDBY_RIDERS_KEY;
	}


	public void insertStandbyRiderWhenStartWork(RiderDTO rider) {
		redisTemplate.opsForHash()
			.put(generateStandbyRiderKey(rider.getAddress()), rider.getId(), rider.getFcmToken());
	}

	public void deleteStandbyRiderWhenStopWork(RiderDTO rider) {
		redisTemplate.opsForHash()
			.delete(generateStandbyRiderKey(rider.getAddress()), rider.getId());
	}

	public void updateStandbyOrderToDelivering(long orderId, RiderDTO rider) {
		String standbyRidersKey = generateStandbyRiderKey(rider.getAddress());
		String standbyOrdersKey = generateStandbyOrderKey(rider.getAddress());
		String orderHashKey = generateOrderHashKey(orderId);

		redisTemplate.execute(new SessionCallback<Object>() {
			@Override
			public Object execute(RedisOperations redisOperations) throws DataAccessException {
				redisOperations.watch(standbyOrdersKey);
				redisOperations.watch(standbyRidersKey);

				redisOperations.multi();

				redisOperations.opsForHash().delete(standbyOrdersKey, orderHashKey);
				redisOperations.opsForHash().delete(standbyRidersKey, rider.getId());

				return redisOperations.exec();
			}
		});
	}

	public void insertStandbyOrderWhenOrderApprove(long orderId, OrderReceiptDTO orderReceipt) {
		redisTemplate.opsForHash()
			.put(generateStandbyOrderKey(orderReceipt.getUserInfo().getAddress()),
				generateOrderHashKey(orderId), orderReceipt);
	}

	public OrderReceiptDTO selectStandbyOrder(long orderId, String riderAddress) {
		return (OrderReceiptDTO) redisTemplate.opsForHash()
			.get(generateStandbyOrderKey(riderAddress), generateOrderHashKey(orderId));
	}

	// 라이더들이 자신의 지역에 배차요청을 기다리는 주문 목록을 보는 메소드
	public List<String> selectStandbyOrderList(String riderAddress) {
		List<String> result = new ArrayList<>();

		redisTemplate.execute((RedisCallback<List<String>>)redisConnection -> {
			ScanOptions options = ScanOptions.scanOptions().match("*").count(200).build();
			Cursor<Entry<byte[], byte[]>> entries = redisConnection.hScan(generateStandbyOrderKey(riderAddress).getBytes(), options);

			while (entries.hasNext()) {
				Entry<byte[], byte[]> entry = entries.next();
				byte[] actualKey = entry.getKey();
				result.add(new String(actualKey));
			}

			return result;
		});
		return result;
	}
}
