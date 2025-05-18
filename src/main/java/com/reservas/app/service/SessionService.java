package com.reservas.app.service;

import com.reservas.app.entity.Session;
import com.reservas.app.entity.AppUser;
import com.reservas.app.repository.SessionRepository;
import com.reservas.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sesionRepo;

    @Autowired
    private UserRepository usuarioRepo;

    public List<Session> obtenerDisponiblesPorFecha(LocalDate fecha) {
        return sesionRepo.findByFecha(fecha);
    }

    public void reservarSesion(Long idSesion, String emailUsuario) {
        Session sesion = sesionRepo.findById(idSesion).orElseThrow();
        AppUser usuario = usuarioRepo.findByEmail(emailUsuario).orElseThrow();

        if (sesion.getSpots() <= 0) throw new RuntimeException("No hay plazas disponibles");

        sesion.getInscritos().add(usuario);
        sesion.setSpots(sesion.getSpots() - 1);
        sesionRepo.save(sesion);
    }
}
