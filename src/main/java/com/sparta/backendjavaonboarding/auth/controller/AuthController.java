package com.sparta.backendjavaonboarding.auth.controller;

import com.sparta.backendjavaonboarding.auth.dto.request.LoginRequest;
import com.sparta.backendjavaonboarding.auth.dto.request.SignupRequest;
import com.sparta.backendjavaonboarding.auth.dto.response.ApprovalResponse;
import com.sparta.backendjavaonboarding.auth.dto.response.LoginResponse;
import com.sparta.backendjavaonboarding.auth.dto.response.SignupResponse;
import com.sparta.backendjavaonboarding.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest reqDto) {
		SignupResponse resDto = authService.signup(reqDto);
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(resDto);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest reqDto) {
		LoginResponse resDto = authService.login(reqDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(resDto);
	}


	@PatchMapping("/admin/users/{userId}/roles")
	public ResponseEntity<ApprovalResponse> approved(@PathVariable Long userId) {
		ApprovalResponse resDto = authService.approved(userId);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(resDto);
	}

}
