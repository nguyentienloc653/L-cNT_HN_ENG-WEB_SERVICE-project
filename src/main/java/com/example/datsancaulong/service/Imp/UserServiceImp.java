package com.example.datsancaulong.service.Imp;

import com.example.datsancaulong.dto.request.UserCreateRequest;
import com.example.datsancaulong.dto.request.UserUpdateRequest;
import com.example.datsancaulong.dto.response.UserProjectionDTO;
import com.example.datsancaulong.entity.User;
import com.example.datsancaulong.exception.NotFoundException;
import com.example.datsancaulong.repository.UserRepository;
import com.example.datsancaulong.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public Page<User> getListUser(Integer currentPage, Integer pageSize, String keyword) {
        Sort sort = Sort.by("id");
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, sort);
        return userRepository.findAll(pageable);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Người dùng không tồn tại"));
    }

    @Override
    public UserProjectionDTO getUserById(Long id) {
        User user = findById(id);

        return UserProjectionDTO.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    @Override
    public User disableUser(Long id) {
        User user = findById(id);
        user.setEnabled(false);
        return userRepository.save(user);
    }

    @Override
    public User createUser(UserCreateRequest request) {
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .roles(request.getRoles())
                .enabled(true)
                .build();
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, UserUpdateRequest request) {
        User user = findById(id);
        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            user.setFullName(request.getFullName());
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            user.setEmail(request.getEmail());
        }

        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank()) {
            user.setPhoneNumber(request.getPhoneNumber());
        }

        return userRepository.save(user);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof com.example.datsancaulong.security.principal.CustomerUserDetails userDetails)) {
            throw new com.example.datsancaulong.exception.UnauthorizedException("Người dùng chưa đăng nhập");
        }

        User user = findById(userDetails.getUser().getId());
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new com.example.datsancaulong.exception.BadRequestException("Mật khẩu cũ không đúng");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
