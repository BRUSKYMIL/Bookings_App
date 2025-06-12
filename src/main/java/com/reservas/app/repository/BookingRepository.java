package com.reservas.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.reservas.app.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Suma todas las plazas reservadas en una sesión (puede devolver null si no hay reservas)
    @Query("SELECT COALESCE(SUM(b.seats), 0) FROM Booking b WHERE b.session.id = :sessionId")
    Integer sumSeatsBySessionId(@Param("sessionId") Long sessionId);

    Optional<Booking> findByBookingCode(Long bookingCode);
    List<Booking> findByUser_NameIgnoreCaseAndUser_SurnameIgnoreCase(String name, String surname);
    boolean existsByUserIdAndSessionId(Long userId, Long sessionId);
}
