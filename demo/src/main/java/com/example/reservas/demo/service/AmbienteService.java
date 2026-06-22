package com.example.reservas.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.reservas.demo.model.Ambiente;
import com.example.reservas.demo.repository.AmbienteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AmbienteService {

    @Autowired
    private AmbienteRepository ambienteRepository;

    public Ambiente registrar(Ambiente ambiente) {
        return ambienteRepository.save(ambiente);
    }

    public List<Ambiente> listar() {
        return ambienteRepository.findAll();
    }
}