package com.mohdiop.deme_api.service;

import com.mohdiop.deme_api.dto.request.AuthenticationRequest;
import com.mohdiop.deme_api.entity.RefreshToken;
import com.mohdiop.deme_api.entity.User;
import com.mohdiop.deme_api.repository.RefreshTokenRepository;
import com.mohdiop.deme_api.repository.UserRepository;
import com.mohdiop.deme_api.security.JwtService;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;

@Service
public class AuthenticationService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthenticationService(
            JwtService jwtService,
            UserRepository userRepository,
            RefreshTokenRepository refreshTokenRepository
    ) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public TokenPairResponse authenticate(AuthenticationRequest authenticationRequest) {
        User userToAuthenticate = userRepository.findByUserEmail(authenticationRequest.email())
                .orElseThrow(() -> new BadCredentialsException("Email ou mot de passe incorrect."));

        if (BCrypt.checkpw(authenticationRequest.password(), userToAuthenticate.getUserPassword())) {
            String newAccessToken = jwtService.generateAccessToken(
                    userToAuthenticate.getUserId(),
                    userToAuthenticate.getUserRoles()
            );
            String newRefreshToken = jwtService.generateRefreshToken(
                    userToAuthenticate.getUserId(),
                    userToAuthenticate.getUserRoles()
            );
            storeRefreshToken(userToAuthenticate.getUserId(), newRefreshToken);
            return new TokenPairResponse(newAccessToken, newRefreshToken);
        }
        throw new BadCredentialsException("Email ou mot de passe incorrect.");
    }

    @Transactional
    public TokenPairResponse refresh(String refreshToken) {
        if (!jwtService.isValidRefreshToken(refreshToken)) {
            throw new JwtException("");
        }
        Long userId = jwtService.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new JwtException("Utilisateur introuvable."));

        String hashedToken = hashToken(refreshToken);
        refreshTokenRepository.findByUserUserIdAndToken(user.getUserId(), hashedToken)
                .orElseThrow(() -> new JwtException(""));

        String newAccessToken = jwtService.generateAccessToken(user.getUserId(), user.getUserRoles());
        String newRefreshToken = jwtService.generateRefreshToken(user.getUserId(), user.getUserRoles());
        storeRefreshToken(user.getUserId(), newRefreshToken);

        return new TokenPairResponse(newAccessToken, newRefreshToken);
    }

    private void storeRefreshToken(Long userId, String refreshToken) {
        String hashedToken = hashToken(refreshToken);
        long expiryMs = jwtService.getRefreshTokenValidityMs();
        Instant expiresAt = Instant.now().plusMillis(expiryMs);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new JwtException("Utilisateur introuvable."));

        RefreshToken tokenToStore = refreshTokenRepository.findByUserUserId(userId)
                .orElse(new RefreshToken(null, user, Instant.now(), null, ""));

        tokenToStore.setExpiresAt(expiresAt);
        tokenToStore.setToken(hashedToken);

        refreshTokenRepository.save(tokenToStore);
    }

    private String hashToken(String token) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = messageDigest.digest(token.getBytes());
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erreur lors du hash du token.", e);
        }
    }

    public Long getCurrentUserId() {
        return Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    }

    public record TokenPairResponse(String accessToken, String refreshToken) {
    }
}
