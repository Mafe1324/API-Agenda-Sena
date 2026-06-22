package com.example.reservas.demo.dto;

import com.example.reservas.demo.model.TipoAmbiente;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AmbienteRequest(
    @NotBlank(message = "El nombre es obligatorio")
    String nombre,

    @NotNull(message = "El tipo es obligatorio")
    TipoAmbiente tipo,

    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 1, message = "La capacidad debe ser mayor o igual a 1")
    Integer capacidad,

    @NotNull(message = "El estado activo es obligatorio")
    Boolean activo
) {
}
