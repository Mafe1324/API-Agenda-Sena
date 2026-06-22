package com.example.reservas.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.reservas.demo.model.EstadoReserva;
import com.example.reservas.demo.model.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @Query("""
        SELECT r FROM Reserva r
        WHERE r.ambiente.id = :ambienteId
        AND r.estado = :estado
        AND r.fechaInicio < :fin
        AND r.fechaFin > :inicio
        """)
    List<Reserva> findSolapadas(
        @Param("ambienteId") Long ambienteId,
        @Param("estado") EstadoReserva estado,
        @Param("inicio") LocalDateTime inicio,
        @Param("fin") LocalDateTime fin
    );

    List<Reserva> findByAmbienteIdAndEstadoAndFechaInicioBetween(
        Long ambienteId, EstadoReserva estado, LocalDateTime inicio, LocalDateTime fin
    );

    List<Reserva> findByNombreInstructorAndEstadoAndFechaInicioBetween(
        String nombreInstructor, EstadoReserva estado, LocalDateTime inicio, LocalDateTime fin
    );

    List<Reserva> findByEstadoAndFechaInicioLessThanAndFechaFinGreaterThan(
        EstadoReserva estado, LocalDateTime fin, LocalDateTime inicio
    );
}
