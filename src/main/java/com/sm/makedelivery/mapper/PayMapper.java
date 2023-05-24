package com.sm.makedelivery.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.sm.makedelivery.dto.PayDTO;

@Mapper
public interface PayMapper {

	void insertPay(PayDTO pay);

}
