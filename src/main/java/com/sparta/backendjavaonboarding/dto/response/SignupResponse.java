package com.sparta.backendjavaonboarding.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class SignupResponse {

	private String username;
	private String nickname;
	private List<RoleResponse> roles;
}
