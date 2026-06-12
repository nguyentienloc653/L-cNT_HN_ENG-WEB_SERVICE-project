package com.example.datsancaulong.service.Imp;

// ...existing code...
import com.example.datsancaulong.entity.Booking;
import com.example.datsancaulong.enums.BookingStatus;
import com.example.datsancaulong.exception.NotFoundException;
import com.example.datsancaulong.exception.UnauthorizedException;
import com.example.datsancaulong.repository.BookingRepository;
import com.example.datsancaulong.security.principal.CustomerUserDetails;
import org.springframework.security.core.Authentication;
import com.example.datsancaulong.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookingServiceImp implements BookingService {
    private final BookingRepository bookingRepository;

    @Override
    public Page<Booking> getBookingHistory(Long userId, Integer currentPage, Integer pageSize) {
        Pageable pageable = PageRequest.of(Math.max(0, currentPage - 1), pageSize, Sort.by("createdAt").descending());
        Page<Booking> page = bookingRepository.findByUserId(userId, pageable);
        return page;
    }

    @Override
    @Transactional
    public Booking approveBooking(Long bookingId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof CustomerUserDetails userDetails)) {
            throw new UnauthorizedException("Người dùng chưa đăng nhập");
        }

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
        if (!isAdmin) {
            throw new UnauthorizedException("Không có quyền phê duyệt lịch");
        }

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đặt sân"));
        booking.setStatus(BookingStatus.CONFIRMED);
        Booking saved = bookingRepository.save(booking);
        return saved;
    }

    @Override
    @Transactional
    public Booking rejectBooking(Long bookingId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof CustomerUserDetails userDetails)) {
            throw new UnauthorizedException("Người dùng chưa đăng nhập");
        }

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
        if (!isAdmin) {
            throw new UnauthorizedException("Không có quyền từ chối lịch");
        }

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đặt sân"));
        booking.setStatus(BookingStatus.CANCELLED);
        Booking saved = bookingRepository.save(booking);
        return saved;
    }
}

