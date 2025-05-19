package com.taller3.Taller.repository;

import com.taller3.Taller.model.Estudiante;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface EstudianteRepository extends R2dbcRepository<Estudiante, Long> {
    Mono<Estudiante> findByCorreo(String correo);
}