package com.reservas.app.controller;

import com.reservas.app.entity.Session;
import com.reservas.app.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sesiones")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @GetMapping("/disponibles")
    public List<Session> getByDate(@RequestParam("fecha") String date) {
        return sessionService.getBookingsByDate(LocalDate.parse(date));
    }

    @PostMapping("/reservar/{id}")
    public void reservar(@PathVariable Long id, @RequestParam String email) {
        sessionService.bookSession(id, email);
    }
}
