package com.reservas.app.service;

import com.reservas.app.entity.*;
import com.reservas.app.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;

    public Booking createBooking(AppUser user, Session session) {
        Long maxNumber = bookingRepository.findAll().stream()
                .mapToLong(b -> b.getBookingNumber() != null ? b.getBookingNumber() : 0L)
                .max().orElse(0L);

        Booking booking = Booking.builder()
                .user(user)
                .session(session)
                .bookingNumber(maxNumber + 1)
                .bookingDate(LocalDateTime.now())
                .build();

        return bookingRepository.save(booking);
    }

    public List<Booking> searchByNameAndSurname(String name, String surname) {
        return bookingRepository.findByUserNameContainingIgnoreCaseAndUserSurnameContainingIgnoreCase(name, surname);
    }

    public Optional<Booking> searchByBookingNumber(Long bookingNumber) {
        return bookingRepository.findByBookingNumber(bookingNumber);
    }
}
