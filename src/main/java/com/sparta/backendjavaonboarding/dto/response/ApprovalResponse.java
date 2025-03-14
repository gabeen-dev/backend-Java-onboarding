package com.sparta.backendjavaonboarding.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class ApprovalResponse {

	private String username;
	private String nickname;
	private List<RoleResponse> roles;
}
