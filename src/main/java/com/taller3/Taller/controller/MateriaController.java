package com.taller3.Taller.controller;

import com.taller3.Taller.model.Materia;
import com.taller3.Taller.service.MateriaService;
import com.taller3.Taller.service.EstudianteService;
import com.taller3.Taller.repository.NotaRepository;
import reactor.core.publisher.Mono;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MateriaController {

    private final MateriaService materiaService;
    private final EstudianteService estudianteService;
    private final NotaRepository notaRepository;

    @GetMapping("/materias/view")
    public Mono<Rendering> listarMaterias() {
        return materiaService.findAll()
            .collectList()
            .map(materias -> Rendering.view("materias-view")
                .modelAttribute("materias", materias)
                .build());
    }

    @GetMapping("/materias/{id}/estudiantes")
    public Mono<Rendering> listarEstudiantesPorMateria(@PathVariable Long id) {
        return materiaService.findById(id)
            .switchIfEmpty(Mono.error(new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Materia no encontrada")))
            .flatMap(materia -> notaRepository.findByMateriaId(id)
                .flatMap(nota -> estudianteService.findById(nota.getEstudianteId())
                    .map(estudiante -> Map.of(
                        "estudiante", estudiante,
                        "nota", nota
                    ))
                )
                .collectList()
                .map(estudiantesNotas -> Rendering.view("estudiantes-materia") 
                    .modelAttribute("materia", materia)
                    .modelAttribute("estudiantes", estudiantesNotas)
                    .build()
            )
            .onErrorResume(e -> Mono.just(Rendering.view("error")
                .modelAttribute("error", e.getMessage())
                .build())));
    }
}