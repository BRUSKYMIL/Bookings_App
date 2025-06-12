package com.reservas.app.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.reservas.app.entity.AppUser;
import com.reservas.app.entity.Booking;
import com.reservas.app.entity.Session;
import com.reservas.app.repository.AppUserRepository;
import com.reservas.app.repository.BookingRepository;
import com.reservas.app.repository.SessionRepository;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private JavaMailSender mailSender;

    public List<Session> getSessionsByDate(LocalDate date) {
        return sessionRepository.findByDate(date);
    }

    public Booking createBooking(Long sessionId, String email, Integer seats) {
        Session session = sessionRepository.findById(sessionId).orElseThrow();
        AppUser user = appUserRepository.findByEmail(email).orElseThrow();

        if (bookingRepository.existsByUserIdAndSessionId(user.getId(), sessionId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya tienes una reserva para esta sesión.");
        }

        int reserved = bookingRepository.sumSeatsBySessionId(sessionId);
        if (reserved + seats > session.getSpots()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No quedan suficientes plazas disponibles.");
        }

        Long bookingCode = Long.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        double totalPrice = seats * session.getPricePerSeat();

        Booking booking = Booking.builder()
                .user(user)
                .session(session)
                .bookingCode(bookingCode)
                .seats(seats)
                .totalPrice(totalPrice)
                .bookingDate(LocalDateTime.now())
                .build();

        Booking savedBooking = bookingRepository.save(booking);
        sendBookingEmail(user.getEmail(), bookingCode);
        return savedBooking;
    }

    public Optional<Booking> getBookingByCode(Long bookingCode) {
        return bookingRepository.findByBookingCode(bookingCode);
    }

    public List<Booking> searchByNameAndSurname(String name, String surname) {
        return bookingRepository.findByUser_NameIgnoreCaseAndUser_SurnameIgnoreCase(name, surname);
    }

    public void deleteBookingById(Long id) {
        bookingRepository.deleteById(id);
    }

    public Booking updateSeats(Long bookingCode, int newSeats) {
        Booking booking = bookingRepository.findByBookingCode(bookingCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva no encontrada."));

        Session session = booking.getSession();

        int reservedExceptCurrent = bookingRepository.sumSeatsBySessionId(session.getId()) - booking.getSeats();
        if (reservedExceptCurrent + newSeats > session.getSpots()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No quedan suficientes plazas disponibles.");
        }

        booking.setSeats(newSeats);
        booking.setTotalPrice(newSeats * session.getPricePerSeat());
        return bookingRepository.save(booking);
    }

    public void cancelBooking(Long bookingCode) {
        Booking booking = bookingRepository.findByBookingCode(bookingCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva no encontrada."));
        bookingRepository.delete(booking);
    }

    private void sendBookingEmail(String to, Long bookingCode) {
        String link = "http://localhost:8080/booking/manage?code=" + bookingCode;
        String subject = "Tu reserva ha sido realizada";
        String text = "Gracias por tu reserva.\n\n"
                + "Puedes ver o modificar tu reserva en el siguiente enlace:\n"
                + link
                + "\n\nEste es un correo automático, no respondas a esta dirección.";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("noreply@tuapp.com");

        mailSender.send(message);
    }
}
