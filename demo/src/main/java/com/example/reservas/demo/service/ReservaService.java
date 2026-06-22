package com.example.reservas.demo.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.reservas.demo.dto.AmbienteMasUsadoSemanaReporte;
import com.example.reservas.demo.dto.OcupacionAmbienteReporte;
import com.example.reservas.demo.dto.ReservaRequest;
import com.example.reservas.demo.exception.RecursoNoEncontradoException;
import com.example.reservas.demo.exception.ReglaNegocioException;
import com.example.reservas.demo.model.Ambiente;
import com.example.reservas.demo.model.EstadoReserva;
import com.example.reservas.demo.model.Reserva;
import com.example.reservas.demo.repository.AmbienteRepository;
import com.example.reservas.demo.repository.ReservaRepository;

@Service
public class ReservaService {
    
    private final ReservaRepository reservaRepository;
    private final  AmbienteRepository ambienteRepository;

    public ReservaService(ReservaRepository reservaRepository, AmbienteRepository ambienteRepository ){
        this.ambienteRepository = ambienteRepository;
        this.reservaRepository = reservaRepository;
    }

    public Reserva crear(ReservaRequest request) {
        Reserva reserva = new Reserva();
        Ambiente ambienteSolicitud = new Ambiente();
        ambienteSolicitud.setId(request.ambienteId());

        reserva.setAmbiente(ambienteSolicitud);
        reserva.setNombreInstructor(request.nombreInstructor());
        reserva.setFechaInicio(request.fechaInicio());
        reserva.setFechaFin(request.fechaFin());
        reserva.setNumeroAprendices(request.numeroAprendices());

        return crear(reserva);
    }

    private Reserva crear(Reserva reserva) {

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
        long minutos = Duration.between(inicio, fin).toMinutes();
        if (minutos < 60 || minutos > 240) {
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

    public List<OcupacionAmbienteReporte> reporteOcupacion(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaFin.isBefore(fechaInicio)) {
            throw new ReglaNegocioException("La fecha fin debe ser igual o posterior a la fecha inicio");
        }

        LocalDateTime inicioRango = fechaInicio.atStartOfDay();
        LocalDateTime finRango = fechaFin.plusDays(1).atStartOfDay();

        List<Reserva> reservas = reservaRepository
            .findByEstadoAndFechaInicioLessThanAndFechaFinGreaterThan(
                EstadoReserva.ACTIVA, finRango, inicioRango
            );

        Map<Long, List<Reserva>> reservasPorAmbiente = reservas.stream()
            .collect(Collectors.groupingBy(reserva -> reserva.getAmbiente().getId()));

        long dias = Duration.between(inicioRango, finRango).toDays();
        double horasDisponibles = dias * 16.0;

        List<OcupacionAmbienteReporte> reporte = new ArrayList<>();
        for (Ambiente ambiente : ambienteRepository.findAll()) {
            List<Reserva> reservasAmbiente = reservasPorAmbiente
                .getOrDefault(ambiente.getId(), List.of());

            double horasOcupadas = reservasAmbiente.stream()
                .mapToDouble(reserva -> calcularHorasDentroDelRango(reserva, inicioRango, finRango))
                .sum();

            double porcentaje = horasDisponibles == 0
                ? 0
                : (horasOcupadas / horasDisponibles) * 100;

            reporte.add(new OcupacionAmbienteReporte(
                ambiente.getId(),
                ambiente.getNombre(),
                ambiente.getTipo(),
                ambiente.getCapacidad(),
                (long) reservasAmbiente.size(),
                redondear(horasOcupadas),
                redondear(porcentaje)
            ));
        }

        return reporte;
    }

    public AmbienteMasUsadoSemanaReporte ambienteMasUsadoSemana(LocalDate fecha) {
        LocalDate fechaReferencia = fecha == null ? LocalDate.now() : fecha;
        LocalDate semanaInicio = fechaReferencia.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDate semanaFin = fechaReferencia.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));

        LocalDateTime inicioRango = semanaInicio.atStartOfDay();
        LocalDateTime finRango = semanaFin.plusDays(1).atStartOfDay();

        List<Reserva> reservas = reservaRepository
            .findByEstadoAndFechaInicioLessThanAndFechaFinGreaterThan(
                EstadoReserva.ACTIVA, finRango, inicioRango
            );

        if (reservas.isEmpty()) {
            throw new RecursoNoEncontradoException("No hay reservas activas en la semana consultada");
        }

        Map<Ambiente, List<Reserva>> reservasPorAmbiente = reservas.stream()
            .collect(Collectors.groupingBy(Reserva::getAmbiente));

        return reservasPorAmbiente.entrySet().stream()
            .map(entry -> {
                Ambiente ambiente = entry.getKey();
                List<Reserva> reservasAmbiente = entry.getValue();
                double horasOcupadas = reservasAmbiente.stream()
                    .mapToDouble(reserva -> calcularHorasDentroDelRango(reserva, inicioRango, finRango))
                    .sum();

                return new AmbienteMasUsadoSemanaReporte(
                    semanaInicio,
                    semanaFin,
                    ambiente.getId(),
                    ambiente.getNombre(),
                    ambiente.getTipo(),
                    (long) reservasAmbiente.size(),
                    redondear(horasOcupadas)
                );
            })
            .max(Comparator
                .comparing(AmbienteMasUsadoSemanaReporte::reservasActivas)
                .thenComparing(AmbienteMasUsadoSemanaReporte::horasOcupadas))
            .orElseThrow(() -> new RecursoNoEncontradoException("No hay reservas activas en la semana consultada"));
    }

    private double calcularHorasDentroDelRango(
            Reserva reserva,
            LocalDateTime inicioRango,
            LocalDateTime finRango) {

        LocalDateTime inicio = reserva.getFechaInicio().isBefore(inicioRango)
            ? inicioRango
            : reserva.getFechaInicio();
        LocalDateTime fin = reserva.getFechaFin().isAfter(finRango)
            ? finRango
            : reserva.getFechaFin();

        return Duration.between(inicio, fin).toMinutes() / 60.0;
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
