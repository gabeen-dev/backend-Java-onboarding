package com.sparta.backendjavaonboarding.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@ToString
@Builder
@Getter
@Entity(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String username;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String nickname;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserRole role;

	public void defaultRole(UserRole userRole) {
		this.role = userRole;
	}

	public void adminRole() {
		this.role = UserRole.ADMIN;
	}
}
