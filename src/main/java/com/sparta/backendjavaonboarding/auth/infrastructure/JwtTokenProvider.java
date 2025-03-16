package com.sparta.backendjavaonboarding.auth.infrastructure;

import com.sparta.backendjavaonboarding.auth.entity.UserRole;
import com.sparta.backendjavaonboarding.exception.AuthException;
import com.sparta.backendjavaonboarding.exception.ExceptionCode;
import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sparta.backendjavaonboarding.auth.entity.User;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

	@Value("${security.jwt.token.secret-key}")
	private String secretKey;

	@Value("${security.jwt.token.expire-length}")
	private long validityInMilliseconds;

	public String createToken(User user) {
		Claims claims = Jwts.claims().setSubject(user.getUsername());
		claims.put("role", user.getRole());

		Date now = new Date();
		Date validity = new Date(now.getTime() + validityInMilliseconds);

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(validity)
			.signWith(SignatureAlgorithm.HS256, secretKey)
			.compact();
	}

	public Authentication getAuthentication(String token) {
		Claims claims = Jwts.parser()
			.setSigningKey(secretKey)
			.parseClaimsJws(token)
			.getBody();
		String role = claims.get("role", String.class);

		return Authentication.builder()
			.userName(claims.getSubject())
			.role(UserRole.valueOf(role)).build();
	}

	public void  validateToken(String token) {
		Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
		if (claims.getBody().getExpiration().before(new Date())) {
			throw new AuthException(ExceptionCode.ACCESS_DENIED);
		}
	}


	@Getter
	@Builder
	@AllArgsConstructor
	public static class Authentication {
		private String userName;
		private UserRole role;
	}

}

