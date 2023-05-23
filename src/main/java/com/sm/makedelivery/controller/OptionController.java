package com.sm.makedelivery.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sm.makedelivery.annotation.CurrentUserId;
import com.sm.makedelivery.annotation.LoginCheck;
import com.sm.makedelivery.annotation.LoginCheck.UserLevel;
import com.sm.makedelivery.dto.OptionDTO;
import com.sm.makedelivery.service.OptionService;
import com.sm.makedelivery.service.StoreService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/stores/{storeId}/menus/{menuId}/options")
@RequiredArgsConstructor
@Slf4j
public class OptionController {

	private final OptionService optionService;
	private final StoreService storeService;

	@PostMapping
	@LoginCheck(userLevel = UserLevel.OWNER)
	public void registerOptionList(@RequestBody List<OptionDTO> optionList, @PathVariable long storeId, @CurrentUserId String ownerId) {
		log.info("optionList: " + optionList);
		storeService.validateMyStore(storeId, ownerId);
		optionService.registerOptionList(optionList);
	}

	@GetMapping
	public ResponseEntity<List<OptionDTO>> loadOptionList(@PathVariable long menuId) {
		List<OptionDTO> optionList = optionService.loadOptionList(menuId);

		return ResponseEntity.ok().body(optionList);
	}

	@DeleteMapping
	@LoginCheck(userLevel = UserLevel.OWNER)
	public void deleteOptionList(@RequestBody List<OptionDTO> optionList, @PathVariable long storeId, @CurrentUserId String ownerId) {
		storeService.validateMyStore(storeId, ownerId);
		optionService.deleteOptionList(optionList);
	}

}
