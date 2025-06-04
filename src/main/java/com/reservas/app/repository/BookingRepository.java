package com.reservas.app.repository;

import com.reservas.app.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserNameContainingIgnoreCaseAndUserSurnameContainingIgnoreCase(String name, String surname);
    Optional<Booking> findByBookingNumber(Long bookingNumber);
}
