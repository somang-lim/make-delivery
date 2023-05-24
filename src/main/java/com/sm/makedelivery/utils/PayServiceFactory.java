package com.sm.makedelivery.utils;

import org.springframework.stereotype.Component;

import com.sm.makedelivery.dto.PayDTO;
import com.sm.makedelivery.service.CardPayService;
import com.sm.makedelivery.service.DepositPayService;
import com.sm.makedelivery.service.NaverPayService;
import com.sm.makedelivery.service.PayService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PayServiceFactory {

	private final CardPayService cardPayService;
	private final NaverPayService naverPayService;
	private final DepositPayService depositPayService;


	public PayService getPayService(PayDTO.PayType payType) {
		PayService payService;

		switch (payType) {
			case CARD -> payService = cardPayService;
			case NAVER_PAY -> payService = naverPayService;
			case DEPOSIT -> payService = depositPayService;
			default -> throw new IllegalArgumentException();
		}

		return payService;
	}
}
