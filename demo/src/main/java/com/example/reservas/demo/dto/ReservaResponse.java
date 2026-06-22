package com.example.reservas.demo.dto;

import java.time.LocalDateTime;

import com.example.reservas.demo.model.EstadoReserva;
import com.example.reservas.demo.model.Reserva;

public record ReservaResponse(
    Long id,
    AmbienteResponse ambiente,
    String nombreInstructor,
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin,
    Integer numeroAprendices,
    EstadoReserva estado
) {
    public static ReservaResponse fromEntity(Reserva reserva) {
        return new ReservaResponse(
            reserva.getId(),
            AmbienteResponse.fromEntity(reserva.getAmbiente()),
            reserva.getNombreInstructor(),
            reserva.getFechaInicio(),
            reserva.getFechaFin(),
            reserva.getNumeroAprendices(),
            reserva.getEstado()
        );
    }
}
