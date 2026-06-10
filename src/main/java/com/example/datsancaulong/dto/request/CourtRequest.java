package com.example.datsancaulong.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CourtRequest {
    @NotBlank(message = "Tên sân không được để trống")
    private String name;
    @NotBlank(message = "Địa chỉ sân không được để trống")
    private String description;
    private String location;
    private MultipartFile image;
}
