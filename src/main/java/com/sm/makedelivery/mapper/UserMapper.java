package com.sm.makedelivery.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.sm.makedelivery.dto.UserDTO;

/*
	@Mapper
	어노테이션을 사용하면 빈으로 등록되며 Service 단에서 의존 관계를 주입할 수 있습니다.
 */

@Mapper
public interface UserMapper {

	void insertUser(UserDTO user);

	boolean isExistsId(String id);

	UserDTO selectUserById(String id);

	void deleteUser(String id);

	void updateUserPassword(String id, String newPassword);
}
