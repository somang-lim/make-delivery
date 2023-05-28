package com.sm.makedelivery.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sm.makedelivery.annotation.LoginCheck.UserLevel;
import com.sm.makedelivery.dto.UserDTO;
import com.sm.makedelivery.mapper.UserMapper;
import com.sm.makedelivery.utils.PasswordEncryptor;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserMapper userMapper;

	@InjectMocks
	private UserService userService;

	private UserDTO user;


	@BeforeEach
	public void makeUser() {
		user = UserDTO.builder()
			.id("user")
			.password(PasswordEncryptor.encrypt("0987"))
			.email("user@email.com")
			.name("유저_테스트")
			.phone("010-1234-1234")
			.address("경기도")
			.level(UserLevel.USER)
			.build();
	}

	@Test
	@DisplayName("회원가입 성공")
	public void signUpTestWhenSuccess() {
		when(userMapper.isExistsId(user.getId())).thenReturn(false);
		doNothing().when(userMapper).insertUser(any(UserDTO.class));

		userService.signUp(user);

		verify(userMapper).insertUser(any(UserDTO.class));
	}

	@Test
	@DisplayName("회원가입 실패 - 아이디 중복")
	public void signUpTestWhenFailByDuplicationId() {
		when(userMapper.isExistsId(user.getId())).thenReturn(true);

		assertThrows(RuntimeException.class, () -> userService.signUp(user));

		verify(userMapper).isExistsId(user.getId());
	}

	@Test
	@DisplayName("아이디 중복인 경우, Return true")
	public void isExistsIdTestWhenReturnTrue() {
		when(userMapper.isExistsId(user.getId())).thenReturn(true);

		assertEquals(userService.isExistsId(user.getId()), true);

		verify(userMapper).isExistsId(user.getId());
	}

	@Test
	@DisplayName("아이디 중복이 아닌 경우, Return false")
	public void isExistsIdTestWhenReturnFalse() {
		when(userMapper.isExistsId(user.getId())).thenReturn(false);

		assertEquals(userService.isExistsId(user.getId()), false);

		verify(userMapper).isExistsId(user.getId());
	}

	@Test
	@DisplayName("유저 삭제")
	public void deleteUserTestWhenSuccess() {
		when(userMapper.isExistsId(user.getId())).thenReturn(true);
		doNothing().when(userMapper).deleteUser(user.getId());

		userService.deleteUser(user.getId());

		verify(userMapper).deleteUser(user.getId());
	}

	@Test
	@DisplayName("유저 삭제 실패 - 삭제할 아이디가 존재하지 않음")
	public void deleteUserTestWhenFalseByNotExistsId() {
		when(userMapper.isExistsId(user.getId())).thenReturn(false);

		assertThrows(RuntimeException.class, () -> userService.deleteUser(user.getId()));

		verify(userMapper).isExistsId(user.getId());
	}

	@Test
	@DisplayName("유저 비밀번호 변경")
	public void changeUserPasswordTestWhenSuccess() {
		doNothing().when(userMapper).updateUserPassword(any(String.class), any(String.class));

		userService.changeUserPassword(user.getId(), "1234");

		verify(userMapper).updateUserPassword(any(String.class), any(String.class));
	}

}
