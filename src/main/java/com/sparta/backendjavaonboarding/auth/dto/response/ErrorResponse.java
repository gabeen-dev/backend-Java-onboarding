package com.sparta.backendjavaonboarding.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
	private ErrorDetail error;

	@Getter
	@AllArgsConstructor
	public static class ErrorDetail {
		private String code;
		private String message;
	}
}
