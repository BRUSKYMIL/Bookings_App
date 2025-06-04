package com.reservas.app.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reservas.app.entity.AppUser;
import com.reservas.app.entity.Booking;
import com.reservas.app.entity.Session;
import com.reservas.app.repository.BookingRepository;
import com.reservas.app.repository.SessionRepository;
import com.reservas.app.repository.UserRepository;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BookingRepository bookingRepository;

    public List<Session> getBookingsByDate(LocalDate date) {
        return sessionRepo.findByDate(date);
    }

    public Booking bookSession(Long sessionId, String email) {
        Session session = sessionRepo.findById(sessionId).orElseThrow();
        AppUser user = userRepo.findByEmail(email).orElseThrow();

        // Para bookingNumber incremental:
        Long maxBookingNumber = bookingRepository.findAll().stream()
                .mapToLong(b -> b.getBookingNumber() != null ? b.getBookingNumber() : 0L)
                .max().orElse(0L);

        Booking booking = Booking.builder()
                .user(user)
                .session(session)
                .bookingNumber(maxBookingNumber + 1)
                .bookingDate(LocalDateTime.now())
                .build();

        return bookingRepository.save(booking);
    }
}
