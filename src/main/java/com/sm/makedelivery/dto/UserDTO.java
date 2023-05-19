package com.sm.makedelivery.dto;

import java.time.LocalDateTime;

import com.sm.makedelivery.annotation.LoginCheck.UserLevel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/*
	@Builder
	DTO 생성할 때, 빌더 클래스를 자동으로 추가합니다.
	빌더 클래스는 멤버변수 별 메소드가 있고 변수에 값을 set 하고 이 값을 통하여
	build() 메소드를 통해 멤버변수에 필수값들을 null 체크하고
	이 멤버변수 값을 이용해 빌더 클래스의 생성자를 호출하고 인스턴스를 리턴합니다.
 */

@Getter
@AllArgsConstructor
@Builder
public class UserDTO {

	private final String id;

	private final String password;

	private final String email;

	private final String name;

	private final String phone;

	private final String address;

	private final UserLevel level;

	private final LocalDateTime createdAt;

	private final LocalDateTime updatedAt;

}
