package com.sm.makedelivery.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.sm.makedelivery.dto.OrderMenuOptionDTO;

@Mapper
public interface OrderMenuOptionMapper {

	void insertOrderMenuOption(List<OrderMenuOptionDTO> orderMenuOptionList);

}
