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

    @GetMapping("/materias/nuevo")
    public Mono<Rendering> mostrarFormularioCreacion() {
        return Mono.just(Rendering.view("materia-form")
            .modelAttribute("materia", new Materia())
            .modelAttribute("modo", "crear")
            .build());
    }

    @PostMapping("/materias/guardar")
    public Mono<Rendering> guardarMateria(@ModelAttribute Materia materia) {
        return materiaService.save(materia)
            .then(Mono.just(Rendering.redirectTo("/materias/view").build()))
            .onErrorResume(e -> Mono.just(Rendering.view("error")
                .modelAttribute("error", "Error al guardar la materia: " + e.getMessage())
                .build()));
    }

    @GetMapping("/materias/editar/{id}")
    public Mono<Rendering> mostrarFormularioEdicion(@PathVariable Long id) {
        return materiaService.findById(id)
            .map(materia -> Rendering.view("materia-form")
                .modelAttribute("materia", materia)
                .modelAttribute("modo", "editar")
                .build())
            .switchIfEmpty(Mono.error(new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Materia no encontrada")));
    }

    @GetMapping("/materias/eliminar/{id}")
    public Mono<Rendering> eliminarMateria(@PathVariable Long id) {
        return materiaService.deleteById(id)
            .then(Mono.just(Rendering.redirectTo("/materias/view").build()))
            .onErrorResume(e -> Mono.just(Rendering.view("error")
                .modelAttribute("error", "Error al eliminar la materia: " + e.getMessage())
                .build()));
    }

    @GetMapping("/materias/{id}/estudiantes")
    public Mono<Rendering> listarEstudiantesPorMateria(@PathVariable Long id) {
        return materiaService.findById(id)
            .switchIfEmpty(Mono.error(new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Materia no encontrada")))
            .flatMap(materia -> notaRepository.findByMateriaId(id)
                .flatMap(nota -> estudianteService.findById(nota.getEstudianteId()))
                .collectList()
                .map(estudiantes -> Rendering.view("estudiantes-view")
                    .modelAttribute("estudiantes", estudiantes)
                    .modelAttribute("materiaId", id)
                    .modelAttribute("materiaNombre", materia.getNombre())
                    .build()
            )
            .onErrorResume(e -> Mono.just(Rendering.view("error")
                .modelAttribute("error", e.getMessage())
                .build())));
    }
}