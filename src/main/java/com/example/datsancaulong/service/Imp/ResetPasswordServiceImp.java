package com.example.datsancaulong.service.Imp;

import com.example.datsancaulong.entity.ResetPasswordToken;
import com.example.datsancaulong.entity.User;
import com.example.datsancaulong.exception.NotFoundException;
import com.example.datsancaulong.repository.ResetPasswordTokenRepository;
import com.example.datsancaulong.repository.UserRepository;
import com.example.datsancaulong.service.ResetPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResetPasswordServiceImp implements ResetPasswordService {
    private final ResetPasswordTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.reset.token.expiration.minutes:60}")
    private Long expirationMinutes;

    @Override
    public String createResetToken(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Người dùng không tồn tại"));

        tokenRepository.deleteByUserId(user.getId());

        String token = UUID.randomUUID().toString();
        ResetPasswordToken entity = ResetPasswordToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(expirationMinutes))
                .build();

        tokenRepository.save(entity);
        return token;
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        ResetPasswordToken t = tokenRepository.findByToken(token).orElseThrow(() -> new NotFoundException("Token không hợp lệ"));

        if (t.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(t);
            throw new com.example.datsancaulong.exception.BadRequestException("Token đã hết hạn");
        }

        User user = t.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        tokenRepository.delete(t);
    }
}

