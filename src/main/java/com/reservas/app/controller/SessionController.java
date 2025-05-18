package com.reservas.app.controller;

import com.reservas.app.entity.Session;
import com.reservas.app.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sesiones")
public class SessionController {

    @Autowired
    private SessionService sesionService;

    @GetMapping("/disponibles")
    public List<Session> listarDisponibles(@RequestParam("fecha") String fecha) {
        return sesionService.obtenerDisponiblesPorFecha(LocalDate.parse(fecha));
    }

    @PostMapping("/reservar/{idSesion}")
    public void reservar(@PathVariable Long idSesion, Principal principal) {
        sesionService.reservarSesion(idSesion, principal.getName());
    }
}
