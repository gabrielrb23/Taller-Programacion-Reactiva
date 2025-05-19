package com.taller3.Taller.service;

import com.taller3.Taller.model.Materia;
import com.taller3.Taller.repository.MateriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MateriaService {
    private final MateriaRepository materiaRepository;

    public Flux<Materia> findAll() {
        return materiaRepository.findAll();
    }

    public Mono<Materia> findById(Long id) {
        return materiaRepository.findById(id);
    }
}