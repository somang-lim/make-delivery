package com.sm.makedelivery.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sm.makedelivery.dto.CartItemDTO;
import com.sm.makedelivery.dto.CartOptionDTO;
import com.sm.makedelivery.dto.OrderDTO;
import com.sm.makedelivery.dto.OrderMenuDTO;
import com.sm.makedelivery.dto.OrderMenuOptionDTO;
import com.sm.makedelivery.dto.PayDTO;
import com.sm.makedelivery.mapper.OrderMapper;
import com.sm.makedelivery.mapper.OrderMenuMapper;
import com.sm.makedelivery.mapper.OrderMenuOptionMapper;
import com.sm.makedelivery.utils.PayServiceFactory;

import lombok.RequiredArgsConstructor;

/*
	OrderTransactionService를 만든 이유
	우선 @Transactiond은 AOP로 구현되어 있기 때문에 RTW 방식으로 프록시를 생성합니다.
	따라서 같은 서비스 내에 있는 메소드를 호출할 때는 Transactional은 어노테이션이 적용되지 않습니다.

	현재 로직에서는 주문과 결제가 같은 트랜잭션에서 처리되어야 하기 때문에, OrderTransactionService를 만들어서 트랜잭션을 분리를 고려했습니다.
	추후에 주문과 결제가 같은 트랜잭션에서 처리되지 않게 변경할 수도 있다는 생각이 들어서
	Transaction 전파 레벨이 변경되어야 할 수 있다고 판단했습니다.

	전파레벨 설정을 기본값으로 해준 이유는 주문 트랜잭션 안에서
	주문, 결제가 모두 이루어져 커밋이 같이 되어야 하고, 롤백도 같이 진행되어야 하기 때문입니다.
	전파레벨 설정이 모두 기본값인 이유는 자식 트랜잭션이 부모 트랜잭션에 합류하여 같은 트랜잭션에서 커밋과 롤백이 되어야 하는 로직이기 때문입니다.
	주문과 결제가 동시에 이루어져야 합니다.
 */

@Service
@Transactional
@RequiredArgsConstructor
public class OrderTransactionService {

	private final OrderMapper orderMapper;
	private final OrderMenuMapper orderMenuMapper;
	private final OrderMenuOptionMapper orderMenuOptionMapper;
	private final PayServiceFactory payServiceFactory;


	public long order(OrderDTO order, List<CartItemDTO> cartList, List<OrderMenuDTO> orderMenuList, List<OrderMenuOptionDTO> orderMenuOptionList) {
		orderMapper.insertOrder(order);
		long totalPrice = registerOrderMenu(cartList, order.getId(), orderMenuList, orderMenuOptionList);

		return totalPrice;
	}

	private long registerOrderMenu(List<CartItemDTO> cartList, long orderId, List<OrderMenuDTO> orderMenuList, List<OrderMenuOptionDTO> orderMenuOptionList) {
		long totalPrice = 0;

		for (CartItemDTO item : cartList) {
			totalPrice += item.getPrice() * item.getCount();

			OrderMenuDTO orderMenu = OrderMenuDTO.builder()
				.orderId(orderId)
				.menuId(item.getMenuId())
				.count(item.getCount())
				.build();

			orderMenuList.add(orderMenu);

			for (CartOptionDTO option : item.getOptionList()) {
				totalPrice += option.getPrice();

				OrderMenuOptionDTO orderMenuOption = OrderMenuOptionDTO.builder()
					.optionId(option.getOptionId())
					.menuId(item.getMenuId())
					.orderId(orderId)
					.build();

				orderMenuOptionList.add(orderMenuOption);
			}
		}

		orderMenuMapper.insertOrderMenu(orderMenuList);
		orderMenuOptionMapper.insertOrderMenuOption(orderMenuOptionList);

		return totalPrice;
	}

	public void pay(PayDTO.PayType payType, long totalPrice, long orderId) {
		PayService payService = payServiceFactory.getPayService(payType);
		payService.pay(totalPrice, orderId);
	}

}
