package com.example.reservas.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.reservas.demo.model.EstadoReserva;
import com.example.reservas.demo.model.Ambiente;
import com.example.reservas.demo.model.TipoAmbiente;

public interface AmbienteRepository extends JpaRepository<Ambiente, Long> {

    @Query("""
        SELECT a FROM Ambiente a
        WHERE a.activo = true
        AND (:tipo IS NULL OR a.tipo = :tipo)
        AND (:capacidadMinima IS NULL OR a.capacidad >= :capacidadMinima)
        AND NOT EXISTS (
            SELECT r FROM Reserva r
            WHERE r.ambiente = a
            AND r.estado = :estado
            AND r.fechaInicio < :fin
            AND r.fechaFin > :inicio
        )
        """)
    List<Ambiente> findDisponibles(
        @Param("inicio") LocalDateTime inicio,
        @Param("fin") LocalDateTime fin,
        @Param("estado") EstadoReserva estado,
        @Param("capacidadMinima") Integer capacidadMinima,
        @Param("tipo") TipoAmbiente tipo
    );
}
