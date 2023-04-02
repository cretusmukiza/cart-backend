package com.kyosk.retailbackend.repository;

import com.kyosk.retailbackend.entity.User;
import com.kyosk.retailbackend.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    Optional<UserSession> findByUser(User user);
}
