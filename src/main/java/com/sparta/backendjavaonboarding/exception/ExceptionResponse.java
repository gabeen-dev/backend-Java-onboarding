package com.sparta.backendjavaonboarding.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ExceptionResponse {

	private ErrorDetail error;

	@Builder
	@Getter
	@AllArgsConstructor
	public static class ErrorDetail {
		private String code;
		private String message;
	}

	public static ExceptionResponse of(AuthException authException) {
		return ExceptionResponse.builder()
			.error(ErrorDetail.builder()
				.code(authException.getExceptionCode().name())
				.message(authException.getMessage())
				.build())
			.build();
	}

	public static ExceptionResponse of(AuthException authException, String message) {
		return ExceptionResponse.builder()
			.error(ErrorDetail.builder()
				.code(authException.getExceptionCode().name())
				.message(message)
				.build())
			.build();
	}

}
