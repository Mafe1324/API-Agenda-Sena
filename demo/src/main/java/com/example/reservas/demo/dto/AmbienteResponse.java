package com.example.reservas.demo.dto;

import com.example.reservas.demo.model.Ambiente;
import com.example.reservas.demo.model.TipoAmbiente;

public record AmbienteResponse(
    Long id,
    String nombre,
    TipoAmbiente tipo,
    Integer capacidad,
    boolean activo
) {
    public static AmbienteResponse fromEntity(Ambiente ambiente) {
        return new AmbienteResponse(
            ambiente.getId(),
            ambiente.getNombre(),
            ambiente.getTipo(),
            ambiente.getCapacidad(),
            ambiente.isActivo()
        );
    }
}
