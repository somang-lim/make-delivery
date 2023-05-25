package com.sm.makedelivery.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sm.makedelivery.dao.DeliveryDAO;
import com.sm.makedelivery.dto.OrderReceiptDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryService {

	private final DeliveryDAO deliveryDAO;


	public void registerStandbyOrderWhenOrderApprove(long orderId, OrderReceiptDTO orderReceipt) {
		deliveryDAO.insertStandbyOrderWhenOrderApprove(orderId, orderReceipt);
	}

	public OrderReceiptDTO loadStandbyOrder(long orderId, String riderAddress) {
		return deliveryDAO.selectStandbyOrder(orderId, riderAddress);
	}

	public List<String> loadStandbyOrderList(String riderAddress) {
		return deliveryDAO.selectStandbyOrderList(riderAddress);
	}

}
