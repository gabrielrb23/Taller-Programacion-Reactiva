package com.taller3.Taller.repository;

import com.taller3.Taller.model.Materia;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface MateriaRepository extends R2dbcRepository<Materia, Long> {
    Mono<Materia> findByNombre(String nombre);
}