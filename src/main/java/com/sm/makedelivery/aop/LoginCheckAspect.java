package com.sm.makedelivery.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import com.sm.makedelivery.service.LoginService;

import lombok.RequiredArgsConstructor;

/*
	Aspect 클래스를 빈으로 등록하는 이유는 타겟을 DI로 받아야 하기 때문입니다.
	ProceedingJoinPoint 같이 타겟 메소드를 알리려면 타겟을 DI로 받아야 합니다.
	InvocationHandler를 이용한 다이나믹 프록시 방식에서 타겟을 직접 DI로 받았지만
	AspectJ에서 @Aspect를 선언하면 자동으로 주입합니다.
 */

@Aspect
@Component
@RequiredArgsConstructor
public class LoginCheckAspect {

	private final LoginService loginService;


	@Before("@annotation(com.sm.makedelivery.annotation.LoginCheck)")
	public void loginCheck() throws HttpClientErrorException {
		String userId = loginService.getCurrentUser();
		if (userId == null) {
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
		}
	}

}
