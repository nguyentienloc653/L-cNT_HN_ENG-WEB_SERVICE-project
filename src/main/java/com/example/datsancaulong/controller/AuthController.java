package com.example.datsancaulong.controller;

import com.example.datsancaulong.dto.request.UserRegister;
import com.example.datsancaulong.dto.response.ApiDataResponse;
import com.example.datsancaulong.entity.User;
import com.example.datsancaulong.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

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
}
