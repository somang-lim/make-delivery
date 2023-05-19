package com.sm.makedelivery.service;

import org.springframework.stereotype.Service;

import com.sm.makedelivery.dto.UserDTO;
import com.sm.makedelivery.exception.DuplicatedIdException;
import com.sm.makedelivery.mapper.UserMapper;
import com.sm.makedelivery.utils.PasswordEncryptor;

import lombok.RequiredArgsConstructor;

/*
	@Service
	비즈니스 로직을 처리하는 서비스라는 것을 알려주는 어노테이션입니다.
	Component Scan을 통해서 @Service 어노테이션이 붙은 클래스를
	스프링이 빈으로 등록하고 이 빈의 라이프사이클을 관리합니다.

	@RequiredArgsConstructor
	final이 붙거나 @NotNull이 붙은 필드의 생성자를 자동 생성해주는 롬복 어노테이션입니다.
	생성자 주입 방식의 의존성 주입을 간단하게 할 수 있습니다.
 */

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserMapper userMapper;


	public void signUp(UserDTO user) {
		if (isExistsId(user.getId())) {
			throw new DuplicatedIdException("Same id exists id: " + user.getId());
		}

		UserDTO encryptedUser = encryptUser(user);
		userMapper.insertUser(encryptedUser);
	}

	private UserDTO encryptUser(UserDTO user) {
		String encryptedPassword = PasswordEncryptor.encrypt(user.getPassword());

		UserDTO encryptedUser = UserDTO.builder()
			.id(user.getId())
			.password(encryptedPassword)
			.email(user.getEmail())
			.name(user.getName())
			.phone(user.getPhone())
			.address(user.getAddress())
			.level(user.getLevel())
			.build();

		return encryptedUser;
	}

	public boolean isExistsId(String id) {
		return userMapper.isExistsId(id);
	}

}
