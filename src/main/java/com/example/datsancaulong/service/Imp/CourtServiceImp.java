package com.example.datsancaulong.service.Imp;

import com.example.datsancaulong.dto.request.BookingRequest;
import com.example.datsancaulong.dto.request.CourtRequest;
import com.example.datsancaulong.entity.Booking;
import com.example.datsancaulong.entity.Court;
import com.example.datsancaulong.enums.BookingStatus;
import com.example.datsancaulong.exception.NotFoundException;
import com.example.datsancaulong.repository.BookingRepository;
import com.example.datsancaulong.repository.CourtRepository;
import com.example.datsancaulong.security.principal.CustomerUserDetails;
import com.example.datsancaulong.service.CourtService;
import com.example.datsancaulong.service.UploadImageService;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourtServiceImp implements CourtService {
    private final CourtRepository courtRepository;
    private final BookingRepository bookingRepository;
    private final UploadImageService uploadImageService;
    @Override
    public Court createCourt(CourtRequest request) {
        Court court = Court.builder()
                .name(request.getName())
                .location(request.getLocation())
                .description(request.getDescription())
                .imageUrl(uploadImageService.uploadImage(request.getImage()))
                .enabled(true)
                .build();

        return courtRepository.save(court);
    }

    @Override
    public Court findCourtById(Long id) {
        return courtRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sân"));
    }

    @Override
    @Transactional
    public Booking bookCourt(Long id, BookingRequest request) {
        if (request == null || request.getStartTime() == null || request.getEndTime() == null) {
            throw new com.example.datsancaulong.exception.BadRequestException("Yêu cầu đặt sân không hợp lệ");
        }

        LocalDateTime start = request.getStartTime();
        LocalDateTime end = request.getEndTime();

        if (!start.isBefore(end)) {
            throw new com.example.datsancaulong.exception.BadRequestException("Thời gian bắt đầu phải trước thời gian kết thúc");
        }

        if (start.isBefore(LocalDateTime.now())) {
            throw new com.example.datsancaulong.exception.BadRequestException("Không thể đặt sân trong quá khứ");
        }
        boolean overlap = bookingRepository.isDuplicateBooking(
                id,
                start,
                end,
                BookingStatus.CANCELLED);

        if (overlap) {
            throw new com.example.datsancaulong.exception.BadRequestException("Khoảng thời gian này đã có người đặt");
        }

        Court court = findCourtById(id);

        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw new com.example.datsancaulong.exception.UnauthorizedException("Người dùng chưa đăng nhập");
        }

        if (!(auth.getPrincipal() instanceof CustomerUserDetails customerUserDetails)) {
            throw new com.example.datsancaulong.exception.UnauthorizedException("Người dùng chưa đăng nhập");
        }
        Booking booking = Booking.builder()
                .court(court)
                .user(customerUserDetails.getUser())
                .startTime(start)
                .endTime(end)
                .status(BookingStatus.PENDING)
                .enabled(true)
                .build();

        return bookingRepository.save(booking);
    }
}
