package com.example.reservas.demo.service;

import com.example.reservas.demo.model.Ambiente;
import com.example.reservas.demo.repository.AmbienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AmbienteService {

    private final AmbienteRepository ambienteRepository;

    public Ambiente registrar(Ambiente ambiente) {
        return ambienteRepository.save(ambiente);
    }

    public List<Ambiente> listar() {
        return ambienteRepository.findAll();
    }
}