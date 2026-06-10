package com.example.datsancaulong.service.Imp;


import com.example.datsancaulong.dto.request.UserRegister;
import com.example.datsancaulong.entity.Role;
import com.example.datsancaulong.entity.User;
import com.example.datsancaulong.repository.UserRepository;
import com.example.datsancaulong.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User register(UserRegister userRegister) {
        User user = User.builder()
                .fullName(userRegister.getFullName())
                .email(userRegister.getEmail())
                .password(passwordEncoder.encode(userRegister.getPassword()))
                .phoneNumber(userRegister.getPhoneNumber())
                .roles(Set.of(Role.builder().id(1L).build()))
                .enabled(true)
                .build();
        return userRepository.save(user);
    }
}
