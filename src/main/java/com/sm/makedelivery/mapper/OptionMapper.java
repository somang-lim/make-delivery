package com.sm.makedelivery.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.sm.makedelivery.dto.OptionDTO;

@Mapper
public interface OptionMapper {

	void insertOptionList(List<OptionDTO> optionList);

	List<OptionDTO> selectOptionList(long menuId);

	void deleteOptionList(List<OptionDTO> optionList);

}
