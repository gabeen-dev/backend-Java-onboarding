package com.sparta.backendjavaonboarding.auth.dto.response;

import lombok.Getter;

@Getter
public class LoginResponse {

	private String token;

	public LoginResponse(String token) {
		this.token = token;
	}
}
