package com.reservas.app.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.reservas.app.entity.Booking;
import com.reservas.app.service.BookingService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final BookingService bookingService;

    @GetMapping("/admin")
    public String adminForm() {
        return "admin";
    }

    @PostMapping("/admin/search")
    public String searchBooking(@RequestParam(required = false) String name,
                                @RequestParam(required = false) String surname,
                                @RequestParam(required = false) Long bookingNumber,
                                Model model) {
        List<Booking> results = new ArrayList<>();
        String mensaje = null;

        if (bookingNumber != null) {
            bookingService.searchByBookingNumber(bookingNumber).ifPresent(results::add);
            if (results.isEmpty()) mensaje = "No se encontró ninguna reserva con ese número.";
        } else if (name != null && !name.isBlank() && surname != null && !surname.isBlank()) {
            results = bookingService.searchByNameAndSurname(name, surname);
            if (results.isEmpty()) mensaje = "No se encontraron reservas con ese nombre y apellido.";
        } else {
            mensaje = "Introduce nombre y apellido o número de reserva para buscar.";
        }

        model.addAttribute("bookings", results);
        model.addAttribute("mensaje", mensaje);
        return "admin_results";
    }
}
