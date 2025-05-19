package com.taller3.Taller.controller;

import com.taller3.Taller.model.Estudiante;
import com.taller3.Taller.service.EstudianteService;
import reactor.core.publisher.Mono;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.stereotype.Controller;

@Controller
public class EstudianteController {

    private final EstudianteService estudianteService;

    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    @GetMapping("/estudiantes/view")
    public Mono<Rendering> getEstudiantesPage() {
        return estudianteService.findAll()
            .collectList()
            .map(estudiantes -> Rendering.view("estudiantes-view")
                .modelAttribute("estudiantes", estudiantes)
                .build()
            );
    }

    @GetMapping("/estudiantes/nuevo")
    public Mono<Rendering> mostrarFormularioNuevoEstudiante() {
        return Mono.just(Rendering.view("nuevo-estudiante")
            .modelAttribute("estudiante", new Estudiante())
            .build());
    }

    @PostMapping("/estudiantes/guardar")
    public Mono<Rendering> guardarEstudiante(@ModelAttribute Estudiante estudiante) {
        return estudianteService.save(estudiante)
            .then(Mono.just(Rendering.redirectTo("/estudiantes/view").build()));
    }

    @GetMapping("/estudiantes/editar/{id}")
    public Mono<Rendering> editarEstudiante(@PathVariable Long id) {
        return estudianteService.findById(id)
            .map(est -> Rendering.view("editar-estudiante")
                .modelAttribute("estudiante", est)
                .build());
    }

    @PostMapping("/estudiantes/actualizar")
    public Mono<Rendering> actualizarEstudiante(@ModelAttribute Estudiante estudiante) {
        return estudianteService.save(estudiante)
            .then(Mono.just(Rendering.redirectTo("/estudiantes/view").build()));
    }

    @GetMapping("/estudiantes/eliminar/{id}")
    public Mono<Rendering> eliminarEstudiante(@PathVariable Long id) {
        return estudianteService.deleteById(id)
            .then(Mono.just(Rendering.redirectTo("/estudiantes/view").build()));
    }
}
