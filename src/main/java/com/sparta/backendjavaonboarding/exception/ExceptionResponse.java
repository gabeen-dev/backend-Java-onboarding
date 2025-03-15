package com.sparta.backendjavaonboarding.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static com.sparta.backendjavaonboarding.exception.ExceptionCode.INTERNAL_SERVER_ERROR;

@Builder
@Getter
@AllArgsConstructor
public class ExceptionResponse {

	private ErrorDetail error;

	public static ExceptionResponse of() {
		 return ExceptionResponse.builder()
			.error(ErrorDetail.builder()
				.code(INTERNAL_SERVER_ERROR.name())
				.message(INTERNAL_SERVER_ERROR.getMessage())
				.build())
			.build();
	}

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
