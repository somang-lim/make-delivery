package com.sm.makedelivery.aop;

import static com.sm.makedelivery.annotation.LoginCheck.*;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import com.sm.makedelivery.annotation.LoginCheck;
import com.sm.makedelivery.service.LoginService;
import com.sm.makedelivery.service.UserService;

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
	private final UserService userService;


	@Before("@annotation(com.sm.makedelivery.annotation.LoginCheck) && @annotation(target)")
	public void loginCheck(LoginCheck target) throws HttpClientErrorException {
		if (target.userLevel() == UserLevel.USER) {
			userLoginCheck();
		} else if (target.userLevel() == UserLevel.OWNER) {
			ownerLoginCheck();
		} else if (target.userLevel() == UserLevel.RIDER) {
			riderLoginCheck();
		}
	}

	private String getCurrentUser() {
		String userId = loginService.getCurrentUser();

		if (userId == null) {
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
		}

		return userId;
	}

	private void userLoginCheck() {
		String userId = getCurrentUser();

		UserLevel level = userService.findUserById(userId).getLevel();

		if (!(level == UserLevel.USER)) {
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
		}
	}

	private void ownerLoginCheck() {
		String userId = getCurrentUser();

		UserLevel level = userService.findUserById(userId).getLevel();

		if (!(level == UserLevel.OWNER)) {
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
		}
	}

	private void riderLoginCheck() {
		String userId = getCurrentUser();

		UserLevel level = userService.findUserById(userId).getLevel();

		if (!(level == UserLevel.RIDER)) {
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
		}
	}

}
