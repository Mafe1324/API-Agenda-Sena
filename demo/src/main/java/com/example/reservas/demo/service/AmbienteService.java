package com.example.reservas.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.reservas.demo.exception.ReglaNegocioException;
import com.example.reservas.demo.model.Ambiente;
import com.example.reservas.demo.model.EstadoReserva;
import com.example.reservas.demo.model.TipoAmbiente;
import com.example.reservas.demo.repository.AmbienteRepository;

@Service
public class AmbienteService {
    
    private final AmbienteRepository ambienteRepository;

    public AmbienteService(AmbienteRepository ambienteRepository) {
        this.ambienteRepository = ambienteRepository;
    }

    public Ambiente registrar(Ambiente ambiente) {
        return ambienteRepository.save(ambiente);
    }

    public List<Ambiente> listar() {
        return ambienteRepository.findAll();
    }

    public List<Ambiente> listarDisponibles(
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin,
            Integer capacidadMinima,
            TipoAmbiente tipo) {

        if (!fechaFin.isAfter(fechaInicio)) {
            throw new ReglaNegocioException("La fecha fin debe ser posterior a la fecha inicio");
        }

        return ambienteRepository.findDisponibles(
            fechaInicio,
            fechaFin,
            EstadoReserva.ACTIVA,
            capacidadMinima,
            tipo
        );
    }
}
