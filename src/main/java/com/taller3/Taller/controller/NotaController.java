package com.taller3.Taller.controller;

import com.taller3.Taller.service.NotaService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;

import reactor.core.publisher.Mono;

@Controller
public class NotaController {
    private final NotaService notaService;

    public NotaController(NotaService notaService) {
        this.notaService = notaService;
    }

    @GetMapping("/notas/{id}")
    public Mono<Rendering> verNotas(@PathVariable Long id) {
        return notaService.findByEstudianteId(id)
            .collectList()
            .map(notas -> Rendering.view("notas-view")
                .modelAttribute("notas", notas)
                .build());
    }
}
