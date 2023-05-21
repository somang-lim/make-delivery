package com.sm.makedelivery.controller;

import static com.sm.makedelivery.annotation.LoginCheck.*;
import static com.sm.makedelivery.utils.ResponseEntityConstants.RESPONSE_OK;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sm.makedelivery.annotation.CurrentUserId;
import com.sm.makedelivery.annotation.LoginCheck;
import com.sm.makedelivery.service.LoginService;
import com.sm.makedelivery.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/my-profiles")
@RequiredArgsConstructor
public class MyProfileController {

	private final LoginService loginService;
	private final UserService userService;


	@DeleteMapping
	@LoginCheck(userLevel = UserLevel.USER)
	public ResponseEntity<Void> deleteUser(@CurrentUserId String userId) {
		userService.deleteUser(userId);
		loginService.logoutUser();

		return RESPONSE_OK;
	}

	@PatchMapping("/password")
	@LoginCheck(userLevel = UserLevel.USER)
	public ResponseEntity<Void> changeUserPassword(@CurrentUserId String userId, String password) {
		userService.changeUserPassword(userId, password);
		return RESPONSE_OK;
	}


}
