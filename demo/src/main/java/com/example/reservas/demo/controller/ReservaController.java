package com.example.reservas.demo.controller;

import java.time.LocalDate;
import java.util.List;

import com.example.reservas.demo.dto.AmbienteMasUsadoSemanaReporte;
import com.example.reservas.demo.dto.OcupacionAmbienteReporte;
import com.example.reservas.demo.dto.ReservaRequest;
import com.example.reservas.demo.dto.ReservaResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.reservas.demo.service.ReservaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    @PostMapping("/api/reservas")
    public ResponseEntity<ReservaResponse> crear(@Valid @RequestBody ReservaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ReservaResponse.fromEntity(reservaService.crear(request)));
    }

    @PatchMapping("/api/reservas/{id}/cancelar")
    public ResponseEntity<ReservaResponse> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(ReservaResponse.fromEntity(reservaService.cancelar(id)));
    }

    @GetMapping("/api/ambientes/{id}/reservas")
    public ResponseEntity<List<ReservaResponse>> reservasPorAmbienteYFecha(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(reservaService.listarPorAmbienteYFecha(id, fecha).stream()
            .map(ReservaResponse::fromEntity)
            .toList());
    }

    @GetMapping("/api/reportes/ocupacion")
    public ResponseEntity<List<OcupacionAmbienteReporte>> reporteOcupacion(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        return ResponseEntity.ok(reservaService.reporteOcupacion(fechaInicio, fechaFin));
    }

    @GetMapping("/api/reportes/ambiente-mas-usado-semana")
    public ResponseEntity<AmbienteMasUsadoSemanaReporte> ambienteMasUsadoSemana(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        return ResponseEntity.ok(reservaService.ambienteMasUsadoSemana(fecha));
    }
}
