package com.taller3.Taller.service;

import com.taller3.Taller.model.Estudiante;
import com.taller3.Taller.repository.EstudianteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class EstudianteService {
    private final EstudianteRepository estudianteRepository;

    public Flux<Estudiante> findAll() {
        return estudianteRepository.findAll();
    }

    public Mono<Estudiante> save(Estudiante estudiante) {
        return estudianteRepository.findByCorreo(estudiante.getCorreo())
            .flatMap(e -> Mono.<Estudiante>error(new RuntimeException("El correo ya existe")))
            .switchIfEmpty(estudianteRepository.save(estudiante));
    }
}