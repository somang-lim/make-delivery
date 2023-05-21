package com.sm.makedelivery.controller;

import static com.sm.makedelivery.annotation.LoginCheck.*;
import static com.sm.makedelivery.utils.ResponseEntityConstants.RESPONSE_CONFLICT;
import static com.sm.makedelivery.utils.ResponseEntityConstants.RESPONSE_NOT_FOUND;
import static com.sm.makedelivery.utils.ResponseEntityConstants.RESPONSE_OK;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sm.makedelivery.annotation.LoginCheck;
import com.sm.makedelivery.dto.UserDTO;
import com.sm.makedelivery.service.LoginService;
import com.sm.makedelivery.service.UserService;

import lombok.RequiredArgsConstructor;

/*
	@RestController
	@Controller + @ResponseBody
	주로 Http Response로 view가 아닌 문자열과 JSON 등을 보낼 때 사용합니다.

	@RequestMapping
	URL을 컨트롤러의 클래스나 메서드와 매핑할 때 사용하는 스프링 프레임워크 어노테이션입니다.

	@RequestBody
	HTTP 요청 body를 자바 객체로 변환합니다.
 */

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final LoginService loginService;


	@PostMapping
	public void signUp(UserDTO user) {
		userService.signUp(user);
	}

	@GetMapping("/{id}/exists")
	public ResponseEntity<Void> checkUniqueId(@PathVariable String id) {
		boolean isUniqueId = userService.isExistsId(id);

		if (!isUniqueId) {
			return RESPONSE_OK;
		} else {
			return RESPONSE_CONFLICT;
		}
	}

	@PostMapping("/login")
	public ResponseEntity<Void> login(String id, String password) {
		Optional<UserDTO> user = userService.findUserByIdAndPassword(id, password);

		if (user.isPresent()) {
			loginService.loginUser(user.get().getId());
			return RESPONSE_OK;
		} else {
			return RESPONSE_NOT_FOUND;
		}
	}

	@GetMapping("/logout")
	@LoginCheck(userLevel = UserLevel.USER)
	public ResponseEntity<Void> logout() {
		loginService.logoutUser();
		return RESPONSE_OK;
	}

}

/*
	컨트롤러 서비스 매퍼(DAO)로 분리해서 사용하는 이유

	객체지향의 원칙을 따르려고 분리하는 것입니다. 비즈니스 로직을 분리하고 재사용을 가능하게 위해서 입니다.
	객체지향의 원칙인 단일책임 원칙을 지켜 서비스는 비즈니스 로직을 처리하게 하고
	컨트롤러는 HTTP 요청에 따라 사용할 서비스를 선택하고 매퍼(DAO)에서는 SQL 쿼리를 분리해서
	각각의 책임을 가지게 하는 것입니다.

	또한 객체지향의 원칙인 개방폐쇄 원칙에 따라 컨트롤러, 서비스, 매퍼가 서로에게 종속되지 않고
	스프링 빈을 통해 객체를 주입받아 객체들끼리 서로 종속되는 일이 없도록 하기 위해서 분리합니다.
	스프링 프레임워크에서 스프링 빈은 객체의 라이프 사이클을 관리하기 때문에
	컨트롤러, 서비스, 매퍼 각각에서 객체를 생성하거나 다른 객체에 의존하지 않고 스프링 프레임워크에 의해서 객체의 라이프사이클을 관리받아야 합니다.
	이러한 원칙들을 지키려면 컨트롤러, 서비스, 매퍼가 각각의 책임과 할일을 분명하게 나눠야 합니다.

	컨트롤러에서 비즈니스 로직을 처리하기 위해 서비스를 불러오고 서비스에서 필요한 매퍼를 호출하여
	DB에 접근하는 식의 계층이 분리된 모델을 사용한다면 중복된 코드도 제거할 수 있습니다.

	컨트롤러에서 여러 서비스들을 재사용하기 용이합니다.
	컨트롤러에서는 서비스 객체를 주입 받아서 사용합니다.
	비즈니스 로직이 데이터를 저장하고 수정하고 제거하는 동작들을 여러 컨트롤러에서 사용할 가능성이 있으므로
	컨트롤러 내에서 처리하지 않고 서비스 객체들에 위임해서 처리하고 컨트롤러는 처리된 결과를 받아 응답하는 것입니다.

	이러한 원칙들을 지킨다면 뷰에 종속되지 않아 웹이든 앱이든 해당 비즈니스 로직을 그대로 가져갈 수 있습니다.
 */
