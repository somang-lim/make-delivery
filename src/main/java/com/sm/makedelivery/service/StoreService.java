package com.sm.makedelivery.service;

import java.util.List;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import com.sm.makedelivery.dto.OrderDTO;
import com.sm.makedelivery.dto.OrderDTO.OrderStatus;
import com.sm.makedelivery.dto.OrderReceiptDTO;
import com.sm.makedelivery.dto.StoreDTO;
import com.sm.makedelivery.exception.StoreNameAlreadyExistsException;
import com.sm.makedelivery.mapper.OrderMapper;
import com.sm.makedelivery.mapper.StoreMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreService {

	private final StoreMapper storeMapper;
	private final OrderMapper orderMapper;
	private final DeliveryService deliveryService;


	@Transactional
	public void insertStore(StoreDTO store, String ownerId) {
		try {
			StoreDTO newStore = setOwnerId(store, ownerId);
			storeMapper.insertStore(newStore);
		} catch (DuplicateKeyException e) {
			throw new StoreNameAlreadyExistsException("Same Store Name " + store.getName());
		}
	}

	private StoreDTO setOwnerId(StoreDTO store, String ownerId) {
		StoreDTO newStore = StoreDTO.builder()
			.name(store.getName())
			.phone(store.getPhone())
			.address(store.getAddress())
			.ownerId(ownerId)
			.introduction(store.getIntroduction())
			.categoryId(store.getCategoryId())
			.build();

		return newStore;
	}

	public List<StoreDTO> getMyAllStore(String ownerId) {
		return storeMapper.selectStoreList(ownerId);
	}

	public boolean isMyStore(long storeId, String ownerId) {
		return storeMapper.isMyStore(storeId, ownerId);
	}

	public void validateMyStore(long storeId, String ownerId) {
		boolean isMyStore = isMyStore(storeId, ownerId);

		if (!isMyStore) {
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
		}
	}

	public StoreDTO getMyStore(long storeId, String ownerId) {
		return storeMapper.selectStore(storeId, ownerId);
	}

	@Transactional
	public void closeMyStore(long storeId) {
		storeMapper.closeMyStore(storeId);
	}

	@Transactional
	public void openMyStore(long storeId) {
		storeMapper.openMyStore(storeId);
	}

	@Transactional
	public void approveOrder(long orderId) {
		orderMapper.approveOrder(orderId, OrderStatus.APPROVED_ORDER);
		OrderReceiptDTO orderReceipt = orderMapper.selectOrderReceipt(orderId);
		deliveryService.registerStandbyOrderWhenOrderApprove(orderId, orderReceipt);
	}

}
