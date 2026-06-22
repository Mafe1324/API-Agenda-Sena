package com.example.reservas.demo.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReservaRequest(
    @NotNull(message = "El ambienteId es obligatorio")
    Long ambienteId,

    @NotBlank(message = "El nombre del instructor es obligatorio")
    String nombreInstructor,

    @NotNull(message = "La fecha de inicio es obligatoria")
    @Future(message = "La fecha de inicio debe ser futura")
    LocalDateTime fechaInicio,

    @NotNull(message = "La fecha fin es obligatoria")
    @Future(message = "La fecha fin debe ser futura")
    LocalDateTime fechaFin,

    @NotNull(message = "El numero de aprendices es obligatorio")
    @Min(value = 1, message = "El numero de aprendices debe ser mayor o igual a 1")
    Integer numeroAprendices
) {
}
