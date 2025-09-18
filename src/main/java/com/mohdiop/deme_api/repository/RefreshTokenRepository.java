package com.mohdiop.deme_api.repository;

import com.mohdiop.deme_api.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserUserIdAndToken(Long userId, String token);
    Optional<RefreshToken> findByUserUserId(Long userId);
}
