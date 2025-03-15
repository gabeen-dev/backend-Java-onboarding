package com.sparta.backendjavaonboarding.auth.dto.request;

import com.sparta.backendjavaonboarding.auth.entity.User;
import com.sparta.backendjavaonboarding.auth.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupRequest {

	private String username;
	private String password;
	private String nickname;

	public User toEntity() {
		User user = User.builder()
			.username(this.username)
			.password(this.password)
			.nickname(this.nickname)
			.build();

		user.defaultRole(UserRole.USER);

		return user;
	}

}
