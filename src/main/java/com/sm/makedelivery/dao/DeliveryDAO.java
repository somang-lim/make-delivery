package com.sm.makedelivery.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
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

		redisTemplate.execute(new SessionCallback<>() {
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

	/*
		selectStandbyOrderList
		라이더들이 자신의 지역에 배차요청을 기다리는 주문 목록을 보는 메소드
	 */
	public List<String> selectStandbyOrderList(String riderAddress) {
		List<String> result = new ArrayList<>();

		redisTemplate.execute((RedisCallback<List<String>>) redisConnection -> {
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

	/*
		Redis 명령어 중 keys를 이용하면 모든 키값들을 가져올 수 있지만, 키 값들을 가져오는 동안 lock이 걸립니다.
		따라서 성능에 영향을 줄 수 있기 떄문에 scan, hscan을 권장합니다.
		Redis는 싱글스레드로 동작하기 때문에 이처럼 어떤 명령어를 O(n)시간 동안 수행하면서 lock이 걸린다면
		그 시간동안 keys 명령어를 수행하기 위해 blocking이 되기 때문입니다.

		scan이 시작된 이후 데이터가 추가된다면 전체 순회가 끝날 때까지 추가된 값은 전체 순회에 포함되지 않습니다.

		selectStandbyRiderTokenList : 같은 지역에 출근한 라이더들에게 푸쉬 메시지를 보내기 위해 같은 지역에 출근한 라이더들의 fcm 토큰 값을 전체 스캔하는 메소드
	 */

	public Set<String> selectStandbyRiderTokenList(String address) {
		Set<String> result = new HashSet<>();

		redisTemplate.execute((RedisCallback<Set<String>>)redisConnection -> {
			ScanOptions options = ScanOptions.scanOptions().match("*").count(200).build();

			Cursor<Entry<byte[], byte[]>> entries = redisConnection.hScan(generateStandbyRiderKey(address).getBytes(), options);

			while (entries.hasNext()) {
				Entry<byte[], byte[]> entry = entries.next();
				byte[] actualValue = entry.getValue();
				result.add(new String(actualValue));
			}

			return result;
		});

		return result;
	}
}
