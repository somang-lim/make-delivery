package com.sm.makedelivery.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.sm.makedelivery.dto.StoreDTO;

@Mapper
public interface StoreMapper {

	void insertStore(StoreDTO newStore);

	List<StoreDTO> selectStoreList(String ownerId);

	boolean isMyStore(Long storeId, String ownerId);

	StoreDTO selectStore(Long storeId, String ownerId);

	void closeMyStore(long storeId);

	void openMyStore(long storeId);

}
