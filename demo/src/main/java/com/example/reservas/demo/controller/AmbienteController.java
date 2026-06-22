package com.example.reservas.demo.controller;

import com.example.reservas.demo.model.Ambiente;
import com.example.reservas.demo.service.AmbienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ambientes")
@RequiredArgsConstructor
public class AmbienteController {

    private final AmbienteService ambienteService;

    @PostMapping
    public ResponseEntity<Ambiente> registrar(@RequestBody Ambiente ambiente) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ambienteService.registrar(ambiente));
    }

    @GetMapping
    public ResponseEntity<List<Ambiente>> listar() {
        return ResponseEntity.ok(ambienteService.listar());
    }
}