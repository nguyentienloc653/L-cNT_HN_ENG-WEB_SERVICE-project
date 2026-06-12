package com.example.datsancaulong.service.Imp;

import com.example.datsancaulong.entity.RefreshToken;
import com.example.datsancaulong.entity.User;
import com.example.datsancaulong.exception.RefreshTokenInvalidException;
import com.example.datsancaulong.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImp {
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh.expiration:604800000}")
    private Long refreshTokenExpiration;

    public RefreshToken generateToken(User user) {
        refreshTokenRepository.deleteByUserId(user.getId());

        String token = UUID.randomUUID().toString();
        Date expiresAt = new Date(System.currentTimeMillis() + refreshTokenExpiration);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .user(user)
                .expiresAt(expiresAt)
                .revoked(false)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiresAt().compareTo(new Date()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RefreshTokenInvalidException("Refresh token hết hạn");
        }
        if (token.getRevoked()) {
            throw new RefreshTokenInvalidException("Refresh token đã bị thu hồi");
        }
        return token;
    }

    public RefreshToken getByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenInvalidException("Refresh token không hợp lệ"));
    }
    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}
