package com.example.reservas.demo.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.reservas.demo.exception.RecursoNoEncontradoException;
import com.example.reservas.demo.exception.ReglaNegocioException;
import com.example.reservas.demo.model.Ambiente;
import com.example.reservas.demo.model.EstadoReserva;
import com.example.reservas.demo.model.Reserva;
import com.example.reservas.demo.repository.AmbienteRepository;
import com.example.reservas.demo.repository.ReservaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservaService {
    
    private ReservaRepository reservaRepository;
    private AmbienteRepository ambienteRepository;

    public ReservaService(ReservaRepository reservaRepository, AmbienteRepository ambienteRepository ){
        this.ambienteRepository = ambienteRepository;
        this.reservaRepository = reservaRepository;
    }

    public Reserva crear(Reserva reserva) {

        Ambiente ambiente = ambienteRepository.findById(reserva.getAmbiente().getId())
            .orElseThrow(() -> new RecursoNoEncontradoException("Ambiente no encontrado"));

        // Regla 4: ambiente inactivo
        if (!ambiente.isActivo()) {
            throw new ReglaNegocioException("El ambiente está inactivo y no puede reservarse");
        }

        LocalDateTime inicio = reserva.getFechaInicio();
        LocalDateTime fin = reserva.getFechaFin();

        // Regla 7: no reservar en el pasado
        if (inicio.isBefore(LocalDateTime.now())) {
            throw new ReglaNegocioException("La fecha de inicio debe ser posterior al momento actual");
        }

        // Regla 3: horario institucional (6:00 - 22:00)
        LocalTime horaInicio = inicio.toLocalTime();
        LocalTime horaFin = fin.toLocalTime();
        if (horaInicio.isBefore(LocalTime.of(6, 0)) || horaFin.isAfter(LocalTime.of(22, 0))) {
            throw new ReglaNegocioException("Las reservas deben estar entre las 6:00 y las 22:00");
        }

        // Regla 3: duración entre 1 y 4 horas
        long horas = Duration.between(inicio, fin).toHours();
        if (horas < 1 || horas > 4) {
            throw new ReglaNegocioException("La reserva debe durar entre 1 y 4 horas");
        }

        // Regla 2: capacidad
        if (reserva.getNumeroAprendices() > ambiente.getCapacidad()) {
            throw new ReglaNegocioException("El número de aprendices supera la capacidad del ambiente ("
                + ambiente.getCapacidad() + ")");
        }

        // Regla 1: sin cruces de horario
        List<Reserva> solapadas = reservaRepository.findSolapadas(
            ambiente.getId(), EstadoReserva.ACTIVA, inicio, fin
        );
        if (!solapadas.isEmpty()) {
            throw new ReglaNegocioException("El ambiente ya tiene una reserva activa que se cruza con ese horario");
        }

        // Regla 5: límite de 3 reservas activas por instructor el mismo día
        LocalDateTime inicioDia = inicio.toLocalDate().atStartOfDay();
        LocalDateTime finDia = inicio.toLocalDate().atTime(23, 59, 59);
        List<Reserva> reservasDelDia = reservaRepository
            .findByNombreInstructorAndEstadoAndFechaInicioBetween(
                reserva.getNombreInstructor(), EstadoReserva.ACTIVA, inicioDia, finDia
            );
        if (reservasDelDia.size() >= 3) {
            throw new ReglaNegocioException("El instructor ya tiene 3 reservas activas ese día");
        }

        reserva.setAmbiente(ambiente);
        reserva.setEstado(EstadoReserva.ACTIVA);
        return reservaRepository.save(reserva);
    }

    public Reserva cancelar(Long id) {
        Reserva reserva = reservaRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Reserva no encontrada"));

        // Regla 6: cancelación con al menos 2 horas de anticipación
        if (LocalDateTime.now().isAfter(reserva.getFechaInicio().minusHours(2))) {
            throw new ReglaNegocioException("Solo se puede cancelar con al menos 2 horas de anticipación");
        }

        reserva.setEstado(EstadoReserva.CANCELADA);
        return reservaRepository.save(reserva);
    }

    public List<Reserva> listarPorAmbienteYFecha(Long ambienteId, LocalDate fecha) {
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.atTime(23, 59, 59);
        return reservaRepository.findByAmbienteIdAndEstadoAndFechaInicioBetween(
            ambienteId, EstadoReserva.ACTIVA, inicio, fin
        );
    }
}
