package com.sparta.backendjavaonboarding.auth.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.backendjavaonboarding.auth.infrastructure.JwtTokenProvider.Authentication;
import com.sparta.backendjavaonboarding.auth.service.AuthService;
import com.sparta.backendjavaonboarding.exception.AuthException;
import com.sparta.backendjavaonboarding.exception.ExceptionResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.List;

import static com.sparta.backendjavaonboarding.exception.ExceptionCode.ACCESS_DENIED;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

	private final ObjectMapper objectMapper;
	private final JwtTokenProvider jwtTokenProvider;
	private final AuthService authService;

	public static final String AUTHORIZATION = "Authorization";
	public static final String BEARER_TYPE = "Bearer";

	private static final List<String> EXCLUDED_PATHS = List.of(
		"/signup",
		"/login"
	);

	@SneakyThrows
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getRequestURI();
		return EXCLUDED_PATHS.stream().anyMatch(path::matches);
	}

	@SneakyThrows
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
		// 유효한 토큰인지
		try {
			String accessToken = extractToken(request.getHeader(AUTHORIZATION));
			jwtTokenProvider.validateToken(accessToken);
			Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
			authService.validateUser(authentication);
		} catch (Exception ex) {
			authExceptionHandler(response);
			return;
		}

		filterChain.doFilter(request, response);
	}

	private void authExceptionHandler(HttpServletResponse response) throws IOException {
		response.setStatus(ACCESS_DENIED.getStatus().value());
		response.setContentType(APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(UTF_8.name());
		response.getWriter().write(objectMapper.writeValueAsString(ExceptionResponse.of(new AuthException(ACCESS_DENIED))));
	}

	private String extractToken(String authHeader) {
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7);
		}
		throw new AuthException(ACCESS_DENIED);
	}

}
