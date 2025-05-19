package com.taller3.Taller.controller;

import com.taller3.Taller.model.Materia;
import com.taller3.Taller.service.MateriaService;
import com.taller3.Taller.service.EstudianteService;
import reactor.core.publisher.Mono;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.stereotype.Controller;

@Controller
public class MateriaController {

    private final MateriaService materiaService;
    private final EstudianteService estudianteService;

    public MateriaController(MateriaService materiaService, EstudianteService estudianteService) {
        this.materiaService = materiaService;
        this.estudianteService = estudianteService;
    }

    @GetMapping("/materias")
    public Mono<Rendering> listarMaterias() {
        return materiaService.findAll()
            .collectList()
            .map(materias -> Rendering.view("materias-view")
                .modelAttribute("materias", materias)
                .build());
    }

    @GetMapping("/materias/{id}/estudiantes")
    public Mono<Rendering> listarEstudiantesPorMateria(@PathVariable Long id) {
        return estudianteService.findByMateriaId(id)
            .collectList()
            .map(estudiantes -> Rendering.view("estudiantes-view")
            .modelAttribute("estudiantes", estudiantes)
            .modelAttribute("materiaId", id)
            .build());
    }
}