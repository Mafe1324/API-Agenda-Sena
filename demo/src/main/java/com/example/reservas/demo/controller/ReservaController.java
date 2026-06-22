package com.example.reservas.demo.controller;

import java.time.LocalDate;
import java.util.List;

import com.example.reservas.demo.dto.OcupacionAmbienteReporte;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.example.reservas.demo.model.Reserva;
import com.example.reservas.demo.service.ReservaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @PostMapping("/api/reservas")
    public ResponseEntity<Reserva> crear(@RequestBody Reserva reserva) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaService.crear(reserva));
    }

    @PatchMapping("/api/reservas/{id}/cancelar")
    public ResponseEntity<Reserva> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.cancelar(id));
    }

    @GetMapping("/api/ambientes/{id}/reservas")
    public ResponseEntity<List<Reserva>> reservasPorAmbienteYFecha(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(reservaService.listarPorAmbienteYFecha(id, fecha));
    }

    @GetMapping("/api/reportes/ocupacion")
    public ResponseEntity<List<OcupacionAmbienteReporte>> reporteOcupacion(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        return ResponseEntity.ok(reservaService.reporteOcupacion(fechaInicio, fechaFin));
    }
}
