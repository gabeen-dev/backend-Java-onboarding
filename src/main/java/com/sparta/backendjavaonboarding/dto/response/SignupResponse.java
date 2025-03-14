package com.sparta.backendjavaonboarding.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class SignupResponse {

	private String username;
	private String nickname;
	private List<RoleResponse> roles;
}
