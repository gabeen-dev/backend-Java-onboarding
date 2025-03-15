package com.sparta.backendjavaonboarding.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.sparta.backendjavaonboarding.exception.ExceptionCode.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(AuthException.class)
	public ResponseEntity<ExceptionResponse> handleExpectedException(AuthException exception) {
		ExceptionResponse response = ExceptionResponse.of(exception);
		return ResponseEntity
			.status(exception.getExceptionCode().getStatus())
			.body(response);
	}


	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ExceptionResponse> handleUnExpectedException(RuntimeException exception) {
		ExceptionResponse response = ExceptionResponse.of();
		return ResponseEntity
			.status(INTERNAL_SERVER_ERROR.getStatus())
			.body(response);
	}

}
