package com.sm.makedelivery.service;

import org.springframework.stereotype.Service;

import com.sm.makedelivery.dto.PayDTO;
import com.sm.makedelivery.dto.PayDTO.PayStatus;
import com.sm.makedelivery.dto.PayDTO.PayType;
import com.sm.makedelivery.mapper.PayMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardPayService implements PayService {

	private final PayMapper payMapper;


	@Override
	public void pay(long price, long orderId) {
		PayDTO pay = PayDTO.builder()
			.payType(PayType.CARD)
			.price(price)
			.orderId(orderId)
			.status(PayStatus.COMPLETE_PAY)
			.build();

		payMapper.insertPay(pay);
	}

}
