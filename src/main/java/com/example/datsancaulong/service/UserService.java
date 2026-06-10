package com.example.datsancaulong.service;

import com.example.datsancaulong.dto.request.UserCreateRequest;
import com.example.datsancaulong.dto.request.UserUpdateRequest;
import com.example.datsancaulong.dto.response.UserProjectionDTO;
import com.example.datsancaulong.entity.User;
import org.springframework.data.domain.Page;

public interface UserService {
    Page<User> getListUser(Integer currentPage, Integer pageSize, String keyword);

    User findById(Long id);

    UserProjectionDTO getUserById(Long id);

    User disableUser(Long id);

    User createUser(UserCreateRequest request);

    User updateUser(Long id, UserUpdateRequest request);
}
