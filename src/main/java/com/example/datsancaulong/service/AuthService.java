package com.example.datsancaulong.service;

import com.example.datsancaulong.dto.request.UserLogin;
import com.example.datsancaulong.dto.request.UserRegister;
import com.example.datsancaulong.dto.response.JwtResponse;
import com.example.datsancaulong.entity.User;

public interface AuthService {
    User register(UserRegister userRegister);
    JwtResponse login(UserLogin userLogin);
}
