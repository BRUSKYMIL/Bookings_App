package com.reservas.app.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reservas.app.entity.Session;
import com.reservas.app.service.SessionService;

/**
 * Controlador para la gestión de sesiones y reservas.
 * Permite consultar sesiones disponibles y reservar una sesión.
 */
@RestController
@RequestMapping("/api/sesiones")
public class SessionController {

    /** Servicio para operaciones relacionadas con sesiones */
    @Autowired
    private SessionService sessionService;

    /**
     * Devuelve la lista de sesiones disponibles para una fecha concreta.
     *
     * @param date Fecha en formato ISO (yyyy-MM-dd).
     * @return Lista de sesiones disponibles para la fecha indicada.
     */
    @GetMapping("/disponibles")
    public List<Session> getByDate(@RequestParam("fecha") String date) {
        return sessionService.getBookingsByDate(LocalDate.parse(date));
    }

    /**
     * Permite reservar una sesión para un usuario.
     *
     * @param id    ID de la sesión a reservar.
     * @param email Email del usuario que realiza la reserva.
     */
    @PostMapping("/reservar/{id}")
    public void reservar(@PathVariable Long id, @RequestParam String email) {
        // El método bookSession ahora devuelve Booking, pero aquí solo reservamos
        sessionService.bookSession(id, email);
    }
}
