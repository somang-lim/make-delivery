package com.sm.makedelivery.service;

import static com.sm.makedelivery.dto.PayDTO.*;

import org.springframework.stereotype.Service;

import com.sm.makedelivery.dto.PayDTO;
import com.sm.makedelivery.dto.PayDTO.PayType;
import com.sm.makedelivery.mapper.PayMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepositPayService implements PayService {

	private final PayMapper payMapper;


	@Override
	public void pay(long price, long orderId) {
		PayDTO pay = builder()
			.payType(PayType.DEPOSIT)
			.price(price)
			.orderId(orderId)
			.status(PayStatus.COMPLETE_PAY)
			.build();

		payMapper.insertPay(pay);
	}

}
