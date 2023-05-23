package com.sm.makedelivery.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.sm.makedelivery.dto.StoreDTO;
import com.sm.makedelivery.dto.StoreCategoryDTO;

@Mapper
public interface StoreListMapper {

	List<StoreCategoryDTO> selectCategoryList();

	List<StoreDTO> selectStoreListByCategory(long categoryId);

	List<StoreDTO> selectStoreListByCategoryAndAddress(long categoryId, String address);

}
