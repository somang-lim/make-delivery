package com.sm.makedelivery.service;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

/*
	SessionLoginService
	HttpSEssion에 직접 의존하지 않습니다.
	스프링이 SessionLoginService에 HttpSession 의존성 주입(DI)을 해줍니다.
	LoginService 인터페이스를 이용해 Session 방식의 LoginService를 구현합니다.
	SessionLoginService는 singleton scope를 가지며 HttpSession은 session scope를 가집니다.
	이러한 스코프 차이 때문에 Spring이 HttpSession 인스턴스를 동적 프록시로 생성하여 주입합니다.
	이러한 기법은 Scoped Proxy라고 합니다.
 */

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

	@Override
	public String getCurrentUser() {
		return (String) session.getAttribute(USER_ID);
	}

}

/*
	레디스의 키값으로 저장되는 타입입니다.
	spring:session:sessions:expires (string) 해당 세션의 만료키로 사용합니다.
	spring:session:expirations (set) expire time에 삭제될 세션 정보를 담고 있습니다.
	spring:session:sessions (hash) session은 map을 저장소로 사용하기 대문에 이곳에 세션 테이터가 있습니다.
 */
