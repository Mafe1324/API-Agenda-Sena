package com.example.reservas.demo.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.reservas.demo.model.Ambiente;
import com.example.reservas.demo.model.TipoAmbiente;
import com.example.reservas.demo.service.AmbienteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ambientes")
@RequiredArgsConstructor
public class AmbienteController {

    @Autowired  
    private AmbienteService ambienteService;

    @PostMapping
    public ResponseEntity<Ambiente> registrar(@RequestBody Ambiente ambiente) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ambienteService.registrar(ambiente));
    }

    @GetMapping
    public ResponseEntity<List<Ambiente>> listar() {
        return ResponseEntity.ok(ambienteService.listar());
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Ambiente>> listarDisponibles(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            @RequestParam(required = false) Integer capacidadMinima,
            @RequestParam(required = false) TipoAmbiente tipo) {

        return ResponseEntity.ok(ambienteService.listarDisponibles(
            fechaInicio,
            fechaFin,
            capacidadMinima,
            tipo
        ));
    }
}
