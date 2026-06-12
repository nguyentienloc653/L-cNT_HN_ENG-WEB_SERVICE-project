package com.example.datsancaulong.controller;

import com.example.datsancaulong.dto.request.RefreshTokenRequest;
import com.example.datsancaulong.dto.request.UserLogin;
import com.example.datsancaulong.dto.request.UserRegister;
import com.example.datsancaulong.dto.request.ForgotPasswordRequest;
import com.example.datsancaulong.dto.request.ResetPasswordRequest;
import com.example.datsancaulong.dto.response.ApiDataResponse;
import com.example.datsancaulong.dto.response.JwtResponse;
import com.example.datsancaulong.entity.RefreshToken;
import com.example.datsancaulong.entity.User;
import com.example.datsancaulong.service.AuthService;
import com.example.datsancaulong.service.Imp.RefreshTokenServiceImp;
import com.example.datsancaulong.security.jwt.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenServiceImp refreshTokenService;
    private final JwtService jwtService;
    private final com.example.datsancaulong.service.ResetPasswordService resetPasswordService;

    @PostMapping("/register")
    public ResponseEntity<ApiDataResponse<User>> handleRegister(@Valid @RequestBody UserRegister  userRegister) {
        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Tạo tài khoan thành công",
                authService.register(userRegister),
                null,
                HttpStatus.CREATED
        ), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiDataResponse<JwtResponse>> handleLogin(@Valid @RequestBody UserLogin userLogin) {
        JwtResponse jwtResponse = authService.login(userLogin);
        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Đăng nhập thành công",
                jwtResponse,
                null,
                HttpStatus.OK
        ), HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiDataResponse<String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        String token = resetPasswordService.createResetToken(request.getEmail());
        // In production do not return token in response; send email instead.
        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Yêu cầu đặt lại mật khẩu đã được tạo",
                token,
                null,
                HttpStatus.OK
        ), HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiDataResponse<String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        resetPasswordService.resetPassword(request.getToken(), request.getNewPassword());
        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Đặt lại mật khẩu thành công",
                "OK",
                null,
                HttpStatus.OK
        ), HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiDataResponse<JwtResponse>> handleRefreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenService.getByToken(request.getRefreshToken());
        refreshTokenService.verifyExpiration(refreshToken);

        String accessToken = jwtService.generateToken(refreshToken.getUser().getEmail());
        JwtResponse jwtResponse = JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();

        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Làm mới token thành công",
                jwtResponse,
                null,
                HttpStatus.OK
        ), HttpStatus.OK);
    }
}
