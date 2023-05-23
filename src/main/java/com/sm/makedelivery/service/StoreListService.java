package com.sm.makedelivery.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sm.makedelivery.dto.StoreCategoryDTO;
import com.sm.makedelivery.dto.StoreDTO;
import com.sm.makedelivery.mapper.StoreListMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreListService {

	private final StoreListMapper storeListMapper;


	@Cacheable(value = "categories", key = "'store_category'")
	public List<StoreCategoryDTO> loadStoreCategory() {
		return storeListMapper.selectCategoryList();
	}

	@Cacheable(value = "stores", key = "#categoryId")
	public List<StoreDTO> loadStoreListByCategory(long categoryId) {
		return storeListMapper.selectStoreListByCategory(categoryId);
	}

	@Cacheable(value = "stores", key = "#address+#categoryId")
	public List<StoreDTO> loadStoreListByCategoryAndAddress(long categoryId, String address) {
		return storeListMapper.selectStoreListByCategoryAndAddress(categoryId, address);
	}

}
