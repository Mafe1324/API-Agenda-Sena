package com.example.reservas.demo.dto;

import com.example.reservas.demo.model.TipoAmbiente;

public record OcupacionAmbienteReporte(
    Long ambienteId,
    String nombreAmbiente,
    TipoAmbiente tipo,
    Integer capacidad,
    Long reservasActivas,
    Double horasOcupadas,
    Double porcentajeOcupacion
) {
}
