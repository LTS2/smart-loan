package com.ysmeta.smartfin.domain.auth;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ysmeta.smartfin.domain.auth.jwt.JwtTokenResponse;
import com.ysmeta.smartfin.domain.auth.service.AuthApplicationService;
import com.ysmeta.smartfin.domain.user.UserDto;
import com.ysmeta.smartfin.util.helper.error.ErrorMessageHelper;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

	private final AuthApplicationService authApplicationService;

	@Autowired
	public AuthController(AuthApplicationService authApplicationService) {
		this.authApplicationService = authApplicationService;
	}

	@PostMapping("/signup")
	public ResponseEntity<String> signUp(@Valid @RequestBody UserDto.CreateRequest userCreateRequestDto,
		BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			String errorMessage = ErrorMessageHelper.getOrDefaultErrorMessage(bindingResult);
			return ResponseEntity.badRequest().body(errorMessage);
		}

		try {
			authApplicationService.signUp(userCreateRequestDto);
			log.info("사용자 {}가 성공적으로 등록되었습니다.", userCreateRequestDto.getEmail());
			return ResponseEntity.status(201).body("회원가입 완료");
		} catch (Exception e) {
			log.error("회원가입 중 오류 발생: ", e);
			return ResponseEntity.status(500).body("회원가입 실패");
		}
	}

	@PostMapping("/login")
	public ResponseEntity<JwtTokenResponse> login(@Valid @RequestBody UserDto.LoginRequest loginRequestDto,
		BindingResult bindingResult, @AuthenticationPrincipal UserDetails userDetails) {
		log.info("III::: userDetails: {}", String.valueOf(userDetails));
		log.info("III::: userDetails: {}", userDetails);
		if (bindingResult.hasErrors()) {
			String errorMessage = ErrorMessageHelper.getOrDefaultErrorMessage(bindingResult);
			JwtTokenResponse errorResponse = JwtTokenResponse.builder()
				.errorMessage(errorMessage)
				.build();
			return ResponseEntity.badRequest().body(errorResponse);
		}
		try {
			JwtTokenResponse jwtTokenResponse = authApplicationService.login(loginRequestDto);
			return ResponseEntity.ok(jwtTokenResponse);
		} catch (Exception e) {
			log.error("로그인 중 오류 발생: ", e);
			JwtTokenResponse errorResponse = JwtTokenResponse.builder()
				.errorMessage("로그인 실패: 이메일 또는 비밀번호가 일치하지 않습니다.")
				.build();
			return ResponseEntity.status(401).body(errorResponse);
		}
	}

	// @PostMapping("/login")
	// public ResponseEntity<JwtResponse> login(@Valid @RequestBody UserDto.LoginRequest loginRequestDto) {
	// 	try {
	// 		// 로그인 서비스 호출
	// 		JwtResponse jwtResponse = authApplicationService.login(loginRequestDto);
	// 		return ResponseEntity.ok(jwtResponse);
	// 	} catch (UserNotFoundException e) {
	// 		log.error("로그인 중 오류 발생 - 사용자 찾을 수 없음: ", e);
	// 		JwtResponse errorResponse = JwtResponse.builder()
	// 			.errorMessage("로그인 실패: 사용자 정보를 찾을 수 없습니다.")
	// 			.build();
	// 		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
	// 	} catch (InvalidPasswordException e) {
	// 		log.error("로그인 중 오류 발생 - 비밀번호 불일치: ", e);
	// 		JwtResponse errorResponse = JwtResponse.builder()
	// 			.errorMessage("로그인 실패: 비밀번호가 일치하지 않습니다.")
	// 			.build();
	// 		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
	// 	} catch (Exception e) {
	// 		log.error("로그인 중 예상치 못한 오류 발생: ", e);
	// 		JwtResponse errorResponse = JwtResponse.builder()
	// 			.errorMessage("로그인 실패: 서버 오류가 발생했습니다.")
	// 			.build();
	// 		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	// 	}
	// }
}
