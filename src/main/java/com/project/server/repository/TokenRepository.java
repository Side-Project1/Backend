package com.project.server.repository;

import com.project.server.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findById(Long id);
    Optional<Token> findByRefreshToken(String refreshToken);
    String deleteByRefreshToken(String refreshToken);
    boolean existsById(Long id);
}