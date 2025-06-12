package com.reservas.app.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.reservas.app.entity.AppUser;
import com.reservas.app.entity.Booking;
import com.reservas.app.entity.Session;
import com.reservas.app.repository.AppUserRepository;
import com.reservas.app.repository.BookingRepository;
import com.reservas.app.repository.SessionRepository;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private AppUserRepository appUserRepository;

    public Booking bookSession(Long sessionId, String email) {
        Session session = sessionRepository.findById(sessionId).orElseThrow();
        AppUser user = appUserRepository.findByEmail(email).orElseThrow();

        if (bookingRepository.existsByUserIdAndSessionId(user.getId(), sessionId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sesión ya reservada por el usuario");
        }

        LocalDateTime now = LocalDateTime.now();
        String codeString = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        Long bookingCode = Long.valueOf(codeString);

        Booking booking = Booking.builder()
                .user(user)
                .session(session)
                .bookingCode(bookingCode)
                .bookingDate(LocalDateTime.now())
                .build();

        return bookingRepository.save(booking);
    }

    public List<Session> getBookingsByDate(LocalDate date) {
        return sessionRepository.findByDate(date);
    }
}