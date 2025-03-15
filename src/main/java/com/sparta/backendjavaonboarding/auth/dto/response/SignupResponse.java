package com.sparta.backendjavaonboarding.auth.dto.response;

import com.sparta.backendjavaonboarding.auth.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class SignupResponse {

	private String username;
	private String nickname;
	private List<RoleResponse> roles;


	public static SignupResponse from(User user) {
		return SignupResponse.builder()
			.username(user.getUsername())
			.nickname(user.getNickname())
			.roles(List.of(RoleResponse.of(user.getRole())))
			.build();
	}

}
