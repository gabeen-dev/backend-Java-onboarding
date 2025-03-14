package com.sparta.backendjavaonboarding.repository;

import com.sparta.backendjavaonboarding.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
