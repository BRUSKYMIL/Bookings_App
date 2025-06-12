package com.reservas.app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.reservas.app.entity.Booking;
import com.reservas.app.service.BookingService;

import lombok.RequiredArgsConstructor;

/**
 * Controlador para la gestión de reservas desde el panel de administración.
 * Permite buscar y eliminar reservas por parte del administrador.
 */
@Controller
@RequiredArgsConstructor
public class AdminController {

    /** Servicio para operaciones relacionadas con reservas */
    private final BookingService bookingService;
    
    /**
     * Muestra el formulario principal de administración.
     *
     * @return Nombre de la vista HTML para el panel de administración.
     */
    @GetMapping("/admin")
    public String showAdminForm() {
        return "admin"; // El nombre de tu HTML de admin
    }

    /**
     * Busca reservas por nombre, apellidos o código de reserva.
     * Si se proporciona un código de reserva válido, busca por código.
     * Si se proporcionan nombre y apellidos, busca por ambos.
     * Los resultados duplicados se eliminan.
     *
     * @param name Nombre del usuario (opcional).
     * @param surname Apellido del usuario (opcional).
     * @param bookingCode Código de reserva (opcional).
     * @param model Modelo para pasar los resultados a la vista.
     * @return Nombre de la vista HTML con los resultados de la búsqueda.
     */
    @PostMapping("/admin/search")
    public String searchBooking(@RequestParam(required = false) String name,
                                @RequestParam(required = false) String surname,
                                @RequestParam(required = false) String bookingCode,
                                Model model) {
        List<Booking> results = new ArrayList<>();

        // Si se proporciona un código de reserva válido, busca por código
        if (bookingCode != null && !bookingCode.isBlank()) {
            try {
                Long code = Long.valueOf(bookingCode);
                Optional<Booking> bookingOpt = bookingService.getBookingByCode(code);
                bookingOpt.ifPresent(results::add);
            } catch (NumberFormatException e) {
                // Ignora códigos inválidos
            }
        }

        // Si se proporcionan nombre y apellidos, busca por ambos
        if ((name != null && !name.isBlank()) && (surname != null && !surname.isBlank())) {
            results.addAll(bookingService.searchByNameAndSurname(name.trim(), surname.trim()));
        }

        // Elimina resultados duplicados
        results = results.stream().distinct().toList();

        model.addAttribute("bookings", results);
        return "admin_results";
    }

    /**
     * Elimina una reserva por su ID.
     *
     * @param id ID de la reserva a eliminar.
     * @param redirectAttributes Atributos para mostrar mensajes tras la redirección.
     * @return Redirección al panel de administración.
     */
    @PostMapping("/admin/deleteBooking")
    public String deleteBooking(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        bookingService.deleteBookingById(id);
        redirectAttributes.addFlashAttribute("mensaje", "Reserva cancelada correctamente.");
        return "redirect:/admin";
    }
}
