package com.taller3.Taller.controller;

import com.taller3.Taller.service.EstudianteService;
import reactor.core.publisher.Mono;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.ui.Model;
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

}
