package com.taller3.Taller.controller;

import com.taller3.Taller.model.Estudiante;
import com.taller3.Taller.service.EstudianteService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {
    private final EstudianteService estudianteService;

    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    @GetMapping
    public Flux<Estudiante> getAll() {
        return estudianteService.findAll();
    }

    @PostMapping
    public Mono<Estudiante> create(@RequestBody Estudiante estudiante) {
        return estudianteService.save(estudiante);
    }
}