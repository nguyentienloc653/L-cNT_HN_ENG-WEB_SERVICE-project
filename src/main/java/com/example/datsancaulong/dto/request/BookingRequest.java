package com.example.datsancaulong.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookingRequest {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd'")
    private LocalDateTime endTime;
}
