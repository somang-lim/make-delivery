package com.sm.makedelivery.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.sm.makedelivery.dto.OrderDTO;

@Mapper
public interface OrderMapper {

	void insertOrder(OrderDTO order);

	void updateTotalPrice(long totalPrice, long orderId);

}
