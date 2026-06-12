package com.example.datsancaulong.repository;

import com.example.datsancaulong.entity.Booking;
import com.example.datsancaulong.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("""
                    select exists(select 1 from Booking b
                    where b.court.id = :courtId
                    and b.startTime < :endTime
                    and b.endTime > :startTime
                    and b.status <> :status)
            """)
    boolean isDuplicateBooking(
            @Param("courtId") Long courtId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("status") BookingStatus status
    );

    org.springframework.data.domain.Page<Booking> findByUserId(Long userId, org.springframework.data.domain.Pageable pageable);

}
