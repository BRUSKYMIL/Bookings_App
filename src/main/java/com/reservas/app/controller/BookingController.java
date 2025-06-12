package com.reservas.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.reservas.app.entity.Booking;
import com.reservas.app.service.BookingService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;

    // Página para gestionar la reserva (ver ticket y formulario de modificación)
    @GetMapping("/manage")
    public String manageBooking(@RequestParam("code") Long bookingCode, Model model) {
        Booking booking = bookingService.getBookingByCode(bookingCode)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada."));
        model.addAttribute("booking", booking);
        return "manage_booking"; // tu vista para gestionar/modificar
    }

    // Modificar número de plazas
    @PostMapping("/updateSeats")
    public String updateSeats(@RequestParam("code") Long bookingCode,
                              @RequestParam("seats") int seats,
                              Model model) {
        Booking updated = bookingService.updateSeats(bookingCode, seats);
        model.addAttribute("booking", updated);
        model.addAttribute("mensaje", "Reserva actualizada correctamente.");
        return "manage_booking";
    }

    // Cancelar reserva
    @PostMapping("/cancel")
    public String cancelBooking(@RequestParam("code") Long bookingCode, Model model) {
        bookingService.cancelBooking(bookingCode);
        model.addAttribute("mensaje", "Reserva cancelada correctamente.");
        return "cancel_success"; // vista simple de confirmación
    }
}
