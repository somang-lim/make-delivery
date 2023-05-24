package com.sm.makedelivery.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sm.makedelivery.dto.StoreCategoryDTO;
import com.sm.makedelivery.dto.StoreDTO;
import com.sm.makedelivery.service.StoreListService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreListController {

	private final StoreListService storeListService;


	@GetMapping("/categories")
	public ResponseEntity<List<StoreCategoryDTO>> loadStoreCategory() {
		List<StoreCategoryDTO> categoryList = storeListService.loadStoreCategory();

		return ResponseEntity.ok(categoryList);
	}

	@GetMapping(params = "categoryId")
	public ResponseEntity<List<StoreDTO>> loadStoreListByCategory(long categoryId) {
		List<StoreDTO> storeList = storeListService.loadStoreListByCategory(categoryId);

		return ResponseEntity.ok(storeList);
	}

	@GetMapping(params = {"categoryId", "address"})
	public ResponseEntity<List<StoreDTO>> loadStoreListByCategoryAndAddress(long categoryId, String address) {
		List<StoreDTO> storeList = storeListService.loadStoreListByCategoryAndAddress(categoryId, address);

		return ResponseEntity.ok(storeList);
	}

}
