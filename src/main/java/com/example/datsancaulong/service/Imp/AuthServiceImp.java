package com.example.datsancaulong.service.Imp;


import com.example.datsancaulong.dto.request.UserLogin;
import com.example.datsancaulong.dto.request.UserRegister;
import com.example.datsancaulong.dto.response.JwtResponse;
import com.example.datsancaulong.entity.RefreshToken;
import com.example.datsancaulong.entity.Role;
import com.example.datsancaulong.entity.User;
import com.example.datsancaulong.repository.UserRepository;
import com.example.datsancaulong.security.jwt.JwtService;
import com.example.datsancaulong.security.principal.CustomerUserDetails;
import com.example.datsancaulong.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final com.example.datsancaulong.security.jwt.JwtService jwtService;
    private final RefreshTokenServiceImp refreshTokenService;

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

    @Override
    public JwtResponse login(UserLogin userLogin) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userLogin.getEmail(), userLogin.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        CustomerUserDetails customerUserDetails = (CustomerUserDetails) authentication.getPrincipal();

        String accessToken = jwtService.generateToken(customerUserDetails.getUsername());
        RefreshToken refreshToken = refreshTokenService.generateToken(customerUserDetails.getUser());

        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }
}
