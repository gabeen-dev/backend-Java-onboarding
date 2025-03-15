package com.sparta.backendjavaonboarding.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

	USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 가입된 사용자입니다."),
	INVALID_CREDENTIALS(HttpStatus.BAD_REQUEST, "아이디 또는 비밀번호가 올바르지 않습니다."),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 토큰입니다."),
	ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "접근 권한이 없습니다.");

	private final HttpStatus status;
	private final String message;

}
