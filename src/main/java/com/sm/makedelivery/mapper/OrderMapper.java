package com.sm.makedelivery.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.sm.makedelivery.dto.OrderDTO;
import com.sm.makedelivery.dto.OrderDTO.OrderStatus;
import com.sm.makedelivery.dto.OrderDetailDTO;
import com.sm.makedelivery.dto.OrderStoreDetailDTO;

@Mapper
public interface OrderMapper {

	void insertOrder(OrderDTO order);

	void completeOrder(long totalPrice, long orderId, OrderStatus orderStatus);

	OrderDTO selectOrder(long orderId);

	OrderDetailDTO selectDetailOrder(long orderId);

	List<OrderStoreDetailDTO> selectDetailStoreOrder(long storeId);
}
