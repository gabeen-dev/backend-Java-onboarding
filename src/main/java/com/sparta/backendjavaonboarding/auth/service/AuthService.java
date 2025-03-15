package com.sparta.backendjavaonboarding.auth.service;

import com.sparta.backendjavaonboarding.auth.dto.request.LoginRequest;
import com.sparta.backendjavaonboarding.auth.dto.request.SignupRequest;
import com.sparta.backendjavaonboarding.auth.dto.response.ApprovalResponse;
import com.sparta.backendjavaonboarding.auth.dto.response.LoginResponse;
import com.sparta.backendjavaonboarding.auth.dto.response.SignupResponse;
import com.sparta.backendjavaonboarding.auth.entity.User;
import com.sparta.backendjavaonboarding.auth.infrastructure.JwtTokenProvider;
import com.sparta.backendjavaonboarding.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final JwtTokenProvider jwtTokenProvider;

	public SignupResponse signup(SignupRequest reqDto) {
		//회원가입 실패 (이미 가입된 사용자) 검증 추가
		User signupUser = userRepository.save(reqDto.toEntity());
		log.info(String.valueOf(signupUser));
		return SignupResponse.from(signupUser);
	}

	public LoginResponse login(LoginRequest reqDto) {
		User user = userRepository.findByUsername(reqDto.getUsername())
			                      .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED)); // TODO : 예외 수정
		// TODO : 비밀번호 검증, 암호화

		String token = jwtTokenProvider.createToken(user.getUsername());
		return new LoginResponse(token);
	}

	public ApprovalResponse approved(Long userId) {
		User user = userRepository.findById(userId)
								  .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
		user.adminRole();
		return ApprovalResponse.from(user);
	}

}
