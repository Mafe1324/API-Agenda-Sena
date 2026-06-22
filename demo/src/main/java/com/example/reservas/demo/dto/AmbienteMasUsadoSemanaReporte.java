package com.example.reservas.demo.dto;

import java.time.LocalDate;

import com.example.reservas.demo.model.TipoAmbiente;

public record AmbienteMasUsadoSemanaReporte(
    LocalDate semanaInicio,
    LocalDate semanaFin,
    Long ambienteId,
    String nombreAmbiente,
    TipoAmbiente tipo,
    Long reservasActivas,
    Double horasOcupadas
) {
}
