package com.cretus.retailbackend.repository;

import com.cretus.retailbackend.entity.UserSession;
import com.cretus.retailbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    Optional<UserSession> findByUser(User user);
}
