package com.example.datsancaulong.dto.request;

import com.example.datsancaulong.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserCreateRequest {
    @NotBlank(message = "tên không được để trống")
    private String fullName;
    @NotBlank(message = "email không được để trống")
    private String email;
    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
    private String password;
    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^\\d{10}$", message = "Số điện thoại phải có 10 số")
    private String phoneNumber;
    @NotNull(message = "Vai trò không được để trống")
    private Set<Role> roles;
}
