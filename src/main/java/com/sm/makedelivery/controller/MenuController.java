package com.sm.makedelivery.controller;

import static com.sm.makedelivery.annotation.LoginCheck.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sm.makedelivery.annotation.CurrentUserId;
import com.sm.makedelivery.annotation.LoginCheck;
import com.sm.makedelivery.dto.MenuDTO;
import com.sm.makedelivery.service.MenuService;
import com.sm.makedelivery.service.StoreService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/stores/{storeId}/menus")
@RequiredArgsConstructor
public class MenuController {

	private final MenuService menuService;
	private final StoreService storeService;


	@PostMapping
	@LoginCheck(userLevel = UserLevel.OWNER)
	public void insertMenu(MenuDTO menu, @PathVariable long storeId, @CurrentUserId String ownerId) {
		storeService.validateMyStore(storeId, ownerId);

		MenuDTO newMenu = menuService.setStoreId(menu, storeId);
		menuService.insertMenu(newMenu);
	}

	@DeleteMapping("/{menuId}")
	@LoginCheck(userLevel = UserLevel.OWNER)
	public void deleteMenu(@PathVariable long menuId, @PathVariable long storeId, @CurrentUserId String ownerId) {
		storeService.validateMyStore(storeId, ownerId);

		menuService.deleteMenu(menuId);
	}

	@GetMapping
	public ResponseEntity<List<MenuDTO>> loadStoreMenu(@PathVariable String storeId) {
		List<MenuDTO> menuList = menuService.loadStoreMenu(storeId);

		return ResponseEntity.ok(menuList);
	}

}
