package com.taller3.Taller.controller;

import com.taller3.Taller.model.Nota;
import com.taller3.Taller.service.NotaService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/notas")
public class NotaController {
    private final NotaService notaService;

    public NotaController(NotaService notaService) {
        this.notaService = notaService;
    }

    @PostMapping
    public Mono<Nota> create(@RequestBody Nota nota) {
        return notaService.saveNota(nota);
    }

    @GetMapping("/promedio")
    public Mono<Double> getPromedio(
        @RequestParam Long estudianteId,
        @RequestParam Long materiaId) {
        return notaService.calcularPromedio(estudianteId, materiaId);
    }
}