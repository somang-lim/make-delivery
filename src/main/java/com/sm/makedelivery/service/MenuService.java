package com.sm.makedelivery.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sm.makedelivery.dto.MenuDTO;
import com.sm.makedelivery.exception.NotExistsIdException;
import com.sm.makedelivery.mapper.MenuMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuService {

	private final MenuMapper menuMapper;


	@Transactional
	public void insertMenu(MenuDTO menu) {
		menuMapper.insertMenu(menu);
	}

	public MenuDTO setStoreId(MenuDTO menu, long storeId) {
		MenuDTO newMenu = MenuDTO.builder()
			.name(menu.getName())
			.price(menu.getPrice())
			.photo(menu.getPhoto())
			.description(menu.getDescription())
			.menuGroupId(menu.getMenuGroupId())
			.storeId(storeId)
			.build();

		return newMenu;
	}

	@Transactional
	public void deleteMenu(long menuId) {
		if (!isExistsId(menuId)) {
			throw new NotExistsIdException("Not Exists id = " + menuId);
		}
		menuMapper.deleteMenu(menuId);
	}

	private boolean isExistsId(long menuId) {
		return menuMapper.isExistsId(menuId);
	}

	public List<MenuDTO> loadStoreMenu(String storeId) {
		return menuMapper.selectStoreMenu(storeId);
	}

}
