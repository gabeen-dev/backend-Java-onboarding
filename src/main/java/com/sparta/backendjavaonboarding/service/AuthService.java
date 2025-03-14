package com.sparta.backendjavaonboarding.service;

import com.sparta.backendjavaonboarding.dto.request.SignupRequest;
import com.sparta.backendjavaonboarding.dto.response.RoleResponse;
import com.sparta.backendjavaonboarding.dto.response.SignupResponse;
import com.sparta.backendjavaonboarding.entity.User;
import com.sparta.backendjavaonboarding.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
	private final UserRepository userRepository;

	public SignupResponse signup(SignupRequest reqDto) {

		User user = User.builder()
			.username(reqDto.getUsername())
			.password(reqDto.getPassword())
			.nickname(reqDto.getNickname())
			.build();

		User signupUser = userRepository.save(user);

		return toDto(signupUser);
	}

	public SignupResponse toDto(User user) {
		List<RoleResponse> roleResponses = List.of(RoleResponse.of(user.getRole()));

		return SignupResponse.builder()
			.username(user.getUsername())
			.nickname(user.getNickname())
			.roles(roleResponses)
			.build();
	}
}
