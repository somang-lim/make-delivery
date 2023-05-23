package com.sm.makedelivery.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.sm.makedelivery.dto.MenuDTO;

@Mapper
public interface MenuMapper {

	void insertMenu(MenuDTO newMenu);

	void deleteMenu(Long menuId);

	boolean isExistsId(Long menuId);

	List<MenuDTO> selectStoreMenu(String storeId);

}
