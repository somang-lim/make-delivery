package com.sm.makedelivery.service;

/*
	로그인 하는 로직은 여러 Controller에서 사용할 수 있기 때문에 LoginService로 구현했습니다.
	지금은 세션 방식으로 구현했지만, 나중에 토큰이나 여러 형태로 구현이 가능하기 때문에
	LoginService를 인터페이스로 Loose Coupling을 통해 컨트롤러가 로그인 서비스를 간접적으로 의존하게 했습니다.
	컨트롤러는 어떤 방식(세션, 토큰 등)으로 로그인을 구현했는지 모르며 interface 메소드만 사용하면 됩니다.
 */

public interface LoginService {

	void loginUser(String id);

	void logoutUser();

}
