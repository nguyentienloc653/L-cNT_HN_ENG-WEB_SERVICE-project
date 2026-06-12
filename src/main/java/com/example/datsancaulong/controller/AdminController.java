package com.example.datsancaulong.controller;

import com.example.datsancaulong.dto.request.UserCreateRequest;
import com.example.datsancaulong.dto.request.UserUpdateRequest;
import com.example.datsancaulong.dto.response.ApiDataResponse;
import com.example.datsancaulong.dto.response.UserProjectionDTO;
import com.example.datsancaulong.entity.User;
import com.example.datsancaulong.entity.Booking;
import com.example.datsancaulong.service.UserService;
import com.example.datsancaulong.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final UserService userService;
    private final BookingService bookingService;

    @GetMapping("/users")
    public ResponseEntity<ApiDataResponse<Page<User>>> getListUser(
            @RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "keyword", defaultValue = "") String keyword
    ) {
        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Lấy danh sách thành công",
                userService.getListUser(currentPage, pageSize, keyword),
                null,
                HttpStatus.OK
        ), HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiDataResponse<UserProjectionDTO>> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Lấy thông tin người dùng thành công",
                userService.getUserById(id),
                null,
                HttpStatus.OK
        ), HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<ApiDataResponse<User>> creatUser(@Valid @RequestBody UserCreateRequest request) {
        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Tạo người dùng thành công",
                userService.createUser(request),
                null,
                HttpStatus.CREATED
        ), HttpStatus.CREATED);
    }

    @PatchMapping("users/{id}/updated")
    public ResponseEntity<ApiDataResponse<User>> updateUser(@PathVariable Long id , @RequestBody UserUpdateRequest request) {
        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Cập nhật người dùng thành công",
                userService.updateUser(id, request),
                null,
                HttpStatus.OK
        ), HttpStatus.OK);
    }

    @PatchMapping("/bookings/{id}/approve")
    public ResponseEntity<ApiDataResponse<Booking>> approveBooking(@PathVariable Long id) {
        Booking booking = bookingService.approveBooking(id);
        return new ResponseEntity<>(new ApiDataResponse<>(true, "Duyệt lịch thành công", booking, null, HttpStatus.OK), HttpStatus.OK);
    }

    @PatchMapping("/bookings/{id}/reject")
    public ResponseEntity<ApiDataResponse<Booking>> rejectBooking(@PathVariable Long id) {
        Booking booking = bookingService.rejectBooking(id);
        return new ResponseEntity<>(new ApiDataResponse<>(true, "Từ chối lịch thành công", booking, null, HttpStatus.OK), HttpStatus.OK);
    }

}
