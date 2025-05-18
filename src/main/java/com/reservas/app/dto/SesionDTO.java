package com.reservas.app.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class SesionDTO {
    private Long id;
    private String nombre;
    private String profesor;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private int plazasDisponibles;
}