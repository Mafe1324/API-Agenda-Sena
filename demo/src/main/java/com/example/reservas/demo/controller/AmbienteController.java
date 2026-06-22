package com.example.reservas.demo.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.reservas.demo.dto.AmbienteRequest;
import com.example.reservas.demo.dto.AmbienteResponse;
import com.example.reservas.demo.model.Ambiente;
import com.example.reservas.demo.model.TipoAmbiente;
import com.example.reservas.demo.service.AmbienteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ambientes")
@RequiredArgsConstructor
public class AmbienteController {

    private final AmbienteService ambienteService;

    @PostMapping
    public ResponseEntity<AmbienteResponse> registrar(@Valid @RequestBody AmbienteRequest request) {
        Ambiente ambiente = new Ambiente(
            null,
            request.nombre(),
            request.tipo(),
            request.capacidad(),
            request.activo()
        );

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(AmbienteResponse.fromEntity(ambienteService.registrar(ambiente)));
    }

    @GetMapping
    public ResponseEntity<List<AmbienteResponse>> listar() {
        return ResponseEntity.ok(ambienteService.listar().stream()
            .map(AmbienteResponse::fromEntity)
            .toList());
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<AmbienteResponse>> listarDisponibles(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            @RequestParam(required = false) Integer capacidadMinima,
            @RequestParam(required = false) TipoAmbiente tipo) {

        return ResponseEntity.ok(ambienteService
            .listarDisponibles(fechaInicio, fechaFin, capacidadMinima, tipo)
            .stream()
            .map(AmbienteResponse::fromEntity)
            .toList());
    }
}
