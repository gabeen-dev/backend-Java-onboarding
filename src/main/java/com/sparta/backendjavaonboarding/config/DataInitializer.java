package com.sparta.backendjavaonboarding.config;

import com.sparta.backendjavaonboarding.auth.entity.User;
import com.sparta.backendjavaonboarding.auth.entity.UserRole;
import com.sparta.backendjavaonboarding.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// 초기 데이터 셋팅
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) {
		userRepository.save(User.builder()
								.username("user1")
								.nickname("user1")
								.role(UserRole.ADMIN)
								.password(passwordEncoder.encode("1234")).build());
	}

}