package com.example.datsancaulong.controller;

import com.example.datsancaulong.dto.request.BookingRequest;
import com.example.datsancaulong.dto.request.ChangePasswordRequest;
import com.example.datsancaulong.service.UserService;
import com.example.datsancaulong.dto.response.ApiDataResponse;
import com.example.datsancaulong.entity.Booking;
import com.example.datsancaulong.service.BookingService;
import com.example.datsancaulong.service.CourtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final CourtService courtService;
    private final BookingService bookingService;
    private final UserService userService;

    @PostMapping("/courts/{id}/book")
    public ResponseEntity<ApiDataResponse<Booking>> bookCourt(
            @PathVariable("id") Long courtId,
            @Valid @RequestBody BookingRequest request
    ) {
        Booking booking = courtService.bookCourt(courtId, request);

        ApiDataResponse<Booking> body = ApiDataResponse.<Booking>builder()
                .success(true)
                .message("Đặt sân thành công")
                .data(booking)
                .error(null)
                .status(HttpStatus.CREATED)
                .build();

        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    @GetMapping("/bookings")
    public ResponseEntity<ApiDataResponse<Page<Booking>>> getBookingHistory(
            @RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof com.example.datsancaulong.security.principal.CustomerUserDetails userDetails)) {
            return new ResponseEntity<>(ApiDataResponse.<Page<Booking>>builder()
                    .success(false)
                    .message("Người dùng chưa đăng nhập")
                    .data(null)
                    .error(null)
                    .status(HttpStatus.UNAUTHORIZED)
                    .build(), HttpStatus.UNAUTHORIZED);
        }

        Long userId = userDetails.getUser().getId();
        Page<Booking> page = bookingService.getBookingHistory(userId, currentPage, pageSize);

        ApiDataResponse<Page<Booking>> body = ApiDataResponse.<Page<Booking>>builder()
                .success(true)
                .message("Lịch sử đặt sân")
                .data(page)
                .error(null)
                .status(HttpStatus.OK)
                .build();

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<ApiDataResponse<String>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(request.getOldPassword(), request.getNewPassword());

        ApiDataResponse<String> body = ApiDataResponse.<String>builder()
                .success(true)
                .message("Đổi mật khẩu thành công")
                .data("OK")
                .error(null)
                .status(HttpStatus.OK)
                .build();

        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}
