package com.example.datsancaulong.service;

public interface ResetPasswordService {
    String createResetToken(String email);

    void resetPassword(String token, String newPassword);
}

