package com.sm.makedelivery.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sm.makedelivery.dto.OptionDTO;
import com.sm.makedelivery.mapper.OptionMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OptionService {

	private final OptionMapper optionMapper;


	@Transactional
	public void registerOptionList(List<OptionDTO> optionList) {
		optionMapper.insertOptionList(optionList);
	}

	public List<OptionDTO> loadOptionList(long menuId) {
		return optionMapper.selectOptionList(menuId);
	}

	@Transactional
	public void deleteOptionList(List<OptionDTO> optionList) {
		optionMapper.deleteOptionList(optionList);
	}

}
