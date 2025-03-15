package com.sparta.backendjavaonboarding.dto.response;

import com.sparta.backendjavaonboarding.entity.User;
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
		List<RoleResponse> roleResponses = List.of(RoleResponse.of(user.getRole()));
		return SignupResponse.builder()
			.username(user.getUsername())
			.nickname(user.getNickname())
			.roles(roleResponses)
			.build();
	}

}
