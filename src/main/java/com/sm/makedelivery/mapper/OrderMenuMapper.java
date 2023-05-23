package com.sm.makedelivery.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.sm.makedelivery.dto.OrderMenuDTO;

@Mapper
public interface OrderMenuMapper {

	void insertOrderMenu(List<OrderMenuDTO> orderMenuList);

}
