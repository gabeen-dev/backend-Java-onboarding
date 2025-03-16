package com.sparta.backendjavaonboarding.auth.service;

import com.sparta.backendjavaonboarding.auth.dto.request.LoginRequest;
import com.sparta.backendjavaonboarding.auth.dto.request.SignupRequest;
import com.sparta.backendjavaonboarding.auth.dto.response.ApprovalResponse;
import com.sparta.backendjavaonboarding.auth.dto.response.LoginResponse;
import com.sparta.backendjavaonboarding.auth.dto.response.SignupResponse;
import com.sparta.backendjavaonboarding.auth.entity.User;
import com.sparta.backendjavaonboarding.auth.infrastructure.JwtTokenProvider;
import com.sparta.backendjavaonboarding.auth.infrastructure.JwtTokenProvider.Authentication;
import com.sparta.backendjavaonboarding.auth.repository.UserRepository;
import com.sparta.backendjavaonboarding.exception.AuthException;
import com.sparta.backendjavaonboarding.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sparta.backendjavaonboarding.exception.ExceptionCode.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final PasswordEncoder passwordEncoder;

	public SignupResponse signup(SignupRequest reqDto) {
		if (userRepository.existsByUsername(reqDto.getUsername())) {
			throw new AuthException(USER_ALREADY_EXISTS);
		}

		User signupUser = userRepository.save(reqDto.toEntity(passwordEncoder.encode(reqDto.getPassword())));

		log.info(String.valueOf(signupUser));
		return SignupResponse.from(signupUser);
	}

	public LoginResponse login(LoginRequest reqDto) {
		User user = userRepository.findByUsername(reqDto.getUsername())
								  .orElseThrow(() -> new AuthException(INVALID_CREDENTIALS));

		if (!passwordEncoder.matches(reqDto.getPassword(), user.getPassword())) {
			throw new AuthException(INVALID_CREDENTIALS);
		}

		String token = jwtTokenProvider.createToken(user);
		return new LoginResponse(token);
	}

	public ApprovalResponse approved(Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new AuthException(USER_NOT_FOUND));

		user.adminRole();
		return ApprovalResponse.from(user);
	}

	public void validateUser(Authentication authentication) {
		User user = userRepository.findByUsername(authentication.getUserName())
			 .orElseThrow(() -> new AuthException(USER_NOT_FOUND));

		if (!user.hasAdminRole(authentication.getRole())) {
			throw new AuthException(ExceptionCode.ACCESS_DENIED);
		}
	}

}
