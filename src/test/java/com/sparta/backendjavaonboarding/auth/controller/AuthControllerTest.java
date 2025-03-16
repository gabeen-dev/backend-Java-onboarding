package com.sparta.backendjavaonboarding.auth.controller;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.backendjavaonboarding.auth.dto.request.SignupRequest;
import com.sparta.backendjavaonboarding.auth.dto.response.ApprovalResponse;
import com.sparta.backendjavaonboarding.auth.dto.response.SignupResponse;
import com.sparta.backendjavaonboarding.auth.entity.User;
import com.sparta.backendjavaonboarding.auth.entity.UserRole;
import com.sparta.backendjavaonboarding.auth.infrastructure.JwtTokenProvider;
import com.sparta.backendjavaonboarding.auth.service.AuthService;


@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {

	@MockBean private AuthService authService;

	@Autowired private MockMvc mockMvc;
	@Autowired private ObjectMapper objectMapper;
	@Autowired JwtTokenProvider jwtTokenProvider;

	@Test
	@DisplayName("회원가입 테스트")
	void testSignup() throws Exception {
		String testUser = "testUser";
		String password = "password123";
		String nicName = "kyle";

		SignupRequest request = new SignupRequest(testUser, password, nicName);

		User user = User.builder()
			.id(1L)
			.username("testUser")
			.nickname("kyle")
			.password("password123")
			.role(UserRole.USER)
			.build();

		SignupResponse response = SignupResponse.from(user);

		when(authService.signup(any(SignupRequest.class))).thenReturn(response);

		mockMvc.perform(post("/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.username").value("testUser"));
	}

	@Test
	@DisplayName("사용자 역할 승인 테스트")
	void testApproved() throws Exception {
		Long userId = 1L;
		String testUser = "testUser";
		String password = "password123";
		String nicName = "kyle";
		User user = User.builder()
			.id(userId)
			.username(testUser)
			.nickname(nicName)
			.password(password)
			.role(UserRole.ADMIN)
			.build();

		ApprovalResponse response = ApprovalResponse.from(user);
		when(authService.approved(userId)).thenReturn(response);

		String accessToken = jwtTokenProvider.createToken(user);

		mockMvc.perform(patch("/admin/users/{userId}/roles", userId)
				.header("Authorization", "Bearer " + accessToken) // 헤
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.username").value(nicName))
			.andExpect(jsonPath("$.status").value("APPROVED"));
	}

}