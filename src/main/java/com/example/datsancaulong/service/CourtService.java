package com.example.datsancaulong.service;

import com.example.datsancaulong.dto.request.BookingRequest;
import com.example.datsancaulong.dto.request.CourtRequest;
import com.example.datsancaulong.entity.Booking;
import com.example.datsancaulong.entity.Court;

public interface CourtService {
    Court createCourt(CourtRequest courtRequest);
    Court findCourtById(Long id);
    Booking bookCourt(Long id, BookingRequest request);
}
