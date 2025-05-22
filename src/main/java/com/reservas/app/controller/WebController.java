package com.reservas.app.controller;

import com.reservas.app.entity.AppUser;
import com.reservas.app.entity.Session;
import com.reservas.app.service.SessionService;
import com.reservas.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class WebController {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String buscarForm() {
        return "buscar";
    }

    @PostMapping("/buscar")
    public String buscarPorFecha(@RequestParam("fecha") String fecha, Model model) {
        List<Session> sesiones = sessionService.getBookingsByDate(LocalDate.parse(fecha));
        model.addAttribute("sesiones", sesiones);
        model.addAttribute("fecha", fecha);
        return "resultado";
    }

    @GetMapping("/login")
    public String loginForm(@RequestParam("id") Long idSesion, Model model) {
        model.addAttribute("idSesion", idSesion);
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam Long idSesion,
            Model model
    ) {
        Optional<AppUser> optionalUser = userService.searchByEmail(email);

        if (optionalUser.isPresent() && optionalUser.get().getPassword().equals(password)) {
            sessionService.bookSession(idSesion, email);
            model.addAttribute("mensaje", "Reserva completada correctamente.");
            return "exito";
        } else {
            model.addAttribute("error", "Credenciales incorrectas");
            model.addAttribute("idSesion", idSesion);
            return "login";
        }
    }
}
