package com.reservas.app.service;

import com.reservas.app.entity.AppUser;
import com.reservas.app.entity.Session;
import com.reservas.app.repository.SessionRepository;
import com.reservas.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepo;

    @Autowired
    private UserRepository userRepo;

    public List<Session> getBookingsByDate(LocalDate date) {
        return sessionRepo.findByDate(date);
    }

    public void bookSession(Long sessionId, String email) {
        Session session = sessionRepo.findById(sessionId).orElseThrow();
        AppUser user = userRepo.findByEmail(email).orElseThrow();

        if (session.getSpots() <= 0) throw new RuntimeException("No hay plazas disponibles");

        session.getBooked().add(user);
        session.setSpots(session.getSpots() - 1);
        sessionRepo.save(session);
    }
}
