package com.reservas.app.controller;

import com.reservas.app.entity.AppUser;
import com.reservas.app.entity.Booking;
import com.reservas.app.service.BookingService;
import com.reservas.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class WebController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String buscarForm() {
        return "buscar";
    }

    @PostMapping("/buscar")
    public String buscarPorFecha(@RequestParam("fecha") String fecha, Model model) {
        List<com.reservas.app.entity.Session> sesiones = bookingService.getSessionsByDate(LocalDate.parse(fecha));
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
            @RequestParam("seats") Integer seats,
            Model model
    ) {
        Optional<AppUser> existing = userService.searchByEmail(email);

        AppUser user = existing.orElseGet(() -> {
            AppUser nuevo = new AppUser();
            nuevo.setName(name);
            nuevo.setSurname(surname);
            nuevo.setEmail(email);
            return userService.saveUser(nuevo);
        });

        Booking booking = bookingService.createBooking(sessionId, user.getEmail(), seats);

        model.addAttribute("usuario", user);
        model.addAttribute("booking", booking);
        return "ticket";
    }
}
