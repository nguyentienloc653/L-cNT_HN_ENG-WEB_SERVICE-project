package com.example.datsancaulong.service;

import com.example.datsancaulong.entity.Booking;
import org.springframework.data.domain.Page;

public interface BookingService {
    Page<Booking> getBookingHistory(Long userId, Integer currentPage, Integer pageSize);

    Booking approveBooking(Long bookingId);

    Booking rejectBooking(Long bookingId);
}

