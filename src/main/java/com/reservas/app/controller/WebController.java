package com.reservas.app.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.reservas.app.entity.AppUser;
import com.reservas.app.entity.Session;
import com.reservas.app.service.SessionService;
import com.reservas.app.service.UserService;

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

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(@RequestParam("sessionId") Long sessionId, Model model) {
        model.addAttribute("sessionId", sessionId);
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(
            @RequestParam("sessionId") Long sessionId,
            @RequestParam("name") String name,
            @RequestParam("surname") String surname,
            @RequestParam("email") String email,
            Model model
    ) {
        Optional<AppUser> existing = userService.searchByEmail(email);

        AppUser user = existing.orElseGet(() -> {
            AppUser nuevo = new AppUser();
            nuevo.setName(name + " " + surname);
            nuevo.setEmail(email);
            return userService.saveUser(nuevo);
        });

        sessionService.bookSession(sessionId, user.getEmail());
        model.addAttribute("usuario", user);
        return "ticket";
    }
}
