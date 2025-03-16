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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.backendjavaonboarding.auth.dto.request.LoginRequest;
import com.sparta.backendjavaonboarding.auth.dto.request.SignupRequest;
import com.sparta.backendjavaonboarding.auth.dto.response.ApprovalResponse;
import com.sparta.backendjavaonboarding.auth.dto.response.LoginResponse;
import com.sparta.backendjavaonboarding.auth.dto.response.SignupResponse;
import com.sparta.backendjavaonboarding.auth.entity.User;
import com.sparta.backendjavaonboarding.auth.entity.UserRole;
import com.sparta.backendjavaonboarding.auth.infrastructure.JwtTokenProvider;
import com.sparta.backendjavaonboarding.auth.infrastructure.JwtTokenProvider.Authentication;
import com.sparta.backendjavaonboarding.auth.repository.UserRepository;
import com.sparta.backendjavaonboarding.auth.service.AuthService;
import com.sparta.backendjavaonboarding.exception.AuthException;
import com.sparta.backendjavaonboarding.exception.ExceptionCode;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest
class AuthControllerTest {

	@MockitoBean private AuthService authService;
	@MockitoBean private UserRepository userRepository;

	@Autowired private MockMvc mockMvc;
	@Autowired private ObjectMapper objectMapper;
	@Autowired JwtTokenProvider jwtTokenProvider;


	@Test
	@DisplayName("회원가입에 성공한다.")
	void testSignup() throws Exception {
		// given
		String testUser = "testUser";
		String password = "password123";
		String nickName = "kyle";
		SignupRequest request = new SignupRequest(testUser, password, nickName);
		User user = User.builder()
			.id(1L)
			.username(testUser)
			.nickname(nickName)
			.password(password)
			.role(UserRole.USER)
			.build();

		SignupResponse response = SignupResponse.from(user);
		when(authService.signup(any(SignupRequest.class))).thenReturn(response);


		// when, then
		mockMvc.perform(post("/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.username").value(testUser));
	}

	@Test
	@DisplayName("로그인에 성공한다.")
	void testLogin() throws Exception {
		// given
		String testUser = "testUser";
		String password = "password123";
		LoginRequest loginRequest = new LoginRequest(testUser, password);
		User user = User.builder()
			.id(1L)
			.username("testUser")
			.nickname("kyle")
			.password("password123")
			.role(UserRole.USER)
			.build();

		String token = jwtTokenProvider.createToken(user);
		LoginResponse loginResponse = new LoginResponse(token);
		when(authService.login(any(LoginRequest.class))).thenReturn(loginResponse);

		// when, then
		mockMvc.perform(post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.token").value(token));
	}

	@Test
	@DisplayName("관리자권한의 사용자는 관리자 권한을 부여할 수 있다.")
	void testApproved() throws Exception {
		Long userId = 1L;
		Long targetId = 2L;
		String testUser = "testUser";
		String password = "password123";
		String nickName = "kyle";
		User adminUser = User.builder()
			.id(userId)
			.username(testUser)
			.nickname(nickName)
			.password(password)
			.role(UserRole.ADMIN)
			.build();

		ApprovalResponse response = ApprovalResponse.from(adminUser);
		when(authService.approved(targetId)).thenReturn(response);
		doNothing().when(authService).validateUser(any());
		String accessToken = jwtTokenProvider.createToken(adminUser);

		mockMvc.perform(patch("/admin/users/{userId}/roles", targetId)
				.header("Authorization", "Bearer " + accessToken)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.username").value(testUser))
			.andExpect(jsonPath("$.nickname").value(nickName))
			.andExpect(jsonPath("$.roles[0].role").value(UserRole.ADMIN.name()))
		;
	}

	@Test
	@DisplayName("일반사용자는 관리자 권한을 부여할 수 없다.")
	void testApproveFail() throws Exception {
		Long userId = 1L;
		Long targetId = 2L;
		String testUser = "testUser";
		String password = "password123";
		String nickName = "kyle";
		User user = User.builder()
			.id(userId)
			.username(testUser)
			.nickname(nickName)
			.password(password)
			.role(UserRole.USER)
			.build();

		doThrow(new AuthException(ExceptionCode.ACCESS_DENIED))
			.when(authService).validateUser(any(Authentication.class));

		String accessToken = jwtTokenProvider.createToken(user);

		mockMvc.perform(patch("/admin/users/{userId}/roles", targetId)
				.header("Authorization", "Bearer " + accessToken)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is4xxClientError())
			.andExpect(jsonPath("$.error.code").value(ExceptionCode.ACCESS_DENIED.name()))
			.andExpect(jsonPath("$.error.message").value(ExceptionCode.ACCESS_DENIED.getMessage()))
		;
	}

}