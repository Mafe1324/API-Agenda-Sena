package com.example.reservas.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.reservas.demo.model.Ambiente;

public interface AmbienteRepository extends JpaRepository<Ambiente, Long> {
}
