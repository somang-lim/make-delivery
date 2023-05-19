package com.sm.makedelivery.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SessionLoginService implements LoginService {

	private static final String USER_ID = "USER_ID";
	private final HttpSession session;


	@Override
	public void loginUser(String id) {
		session.setAttribute(USER_ID, id);
	}

	@Override
	public void logoutUser() {
		session.removeAttribute(USER_ID);
	}
}
