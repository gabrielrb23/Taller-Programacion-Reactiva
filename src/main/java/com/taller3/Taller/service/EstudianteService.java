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

    public Mono<Estudiante> findById(Long id) {
        return estudianteRepository.findById(id);
    }
    
    public Mono<Estudiante> save(Estudiante estudiante) {
        return estudianteRepository.findByCorreo(estudiante.getCorreo())
            .flatMap(existing -> {
                if (existing.getId().equals(estudiante.getId())) {
                    return estudianteRepository.save(estudiante);
                } else {
                    return Mono.error(new RuntimeException("El correo ya existe"));
                }
            })
            .switchIfEmpty(estudianteRepository.save(estudiante));
    }

    public Mono<Void> deleteById(Long id) {
        return estudianteRepository.deleteById(id);
    }
}