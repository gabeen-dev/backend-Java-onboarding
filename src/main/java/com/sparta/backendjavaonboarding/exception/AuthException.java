package com.sparta.backendjavaonboarding.exception;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {

	private ExceptionCode exceptionCode;

	public AuthException(String message, ExceptionCode exceptionCode) {
		super(message);
		this.exceptionCode = exceptionCode;
	}

	public AuthException(ExceptionCode exceptionCode) {
		super(exceptionCode.getMessage());
		this.exceptionCode = exceptionCode;
	}

}
