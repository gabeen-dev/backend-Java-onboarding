package com.sparta.backendjavaonboarding.dto.response;

import com.sparta.backendjavaonboarding.entity.UserRole;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RoleResponse {

	private String role;

	public static RoleResponse of(UserRole role) {
		return RoleResponse.builder()
			.role(role.name())
			.build();
	}
}