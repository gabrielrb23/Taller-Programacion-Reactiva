package com.taller3.Taller.controller;
import com.taller3.Taller.model.Nota;
import com.taller3.Taller.service.NotaService;
import java.util.List;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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

    @GetMapping("/notas/{estudianteId}")
    public Mono<Rendering> verNotas(@PathVariable Long estudianteId,
                                @RequestParam Long materiaId) {
        Mono<List<Nota>> notasMono = notaService.findByMateriaIdAndEstudianteId(materiaId, estudianteId)
                .collectList();

        Mono<Double> promedioMono = notaService.calcularPromedio(estudianteId, materiaId)
                .onErrorReturn(0.0);

        return Mono.zip(notasMono, promedioMono)
        .map(tuple -> {
            List<Nota> notas = tuple.getT1();
            Double promedio = tuple.getT2();

            return Rendering.view("notas-view")
                .modelAttribute("notas", notas)
                .modelAttribute("promedio", promedio)
                .modelAttribute("estudianteId", estudianteId)  
                .modelAttribute("materiaId", materiaId)       
                .build();
        });
    }

    @GetMapping("/notas/nuevo")
    public Mono<Rendering> nuevoFormulario(@RequestParam Long estudianteId,
                                        @RequestParam Long materiaId) {
        Nota nota = new Nota();
        nota.setEstudianteId(estudianteId);
        nota.setMateriaId(materiaId);

        return Mono.just(Rendering.view("notas-form")
                .modelAttribute("nota", nota)
                .modelAttribute("modo", "crear")
                .build());
    }


    @PostMapping("/notas/guardar")
    public Mono<Rendering> guardarNota(@ModelAttribute Nota nota) {
        return notaService.saveNota(nota)
            .then(Mono.just(Rendering.redirectTo("/notas/" + nota.getEstudianteId() + "?materiaId=" + nota.getMateriaId()).build()))
            .onErrorResume(e -> {
                String mensajeError = e.getMessage();
                return Mono.just(Rendering.view("notas-form")
                    .modelAttribute("nota", nota)
                    .modelAttribute("modo", nota.getId() == null ? "crear" : "editar")
                    .modelAttribute("error", mensajeError)
                    .build());
            });
    }



    @GetMapping("/notas/editar")
    public Mono<Rendering> editarFormulario(@RequestParam Long id) {
        return notaService.findById(id)
            .flatMap(nota -> Mono.just(Rendering.view("notas-form")
                .modelAttribute("nota", nota)
                .modelAttribute("modo", "editar")
                .build()))
            .switchIfEmpty(Mono.just(Rendering.view("error")
                .modelAttribute("mensaje", "Nota no encontrada")
                .build()));
    }


    @GetMapping("/notas/eliminar")
    public Mono<String> eliminarNota(@RequestParam Long id, @RequestParam Long estudianteId, @RequestParam Long materiaId) {
        return notaService.deleteById(id)
            .thenReturn("redirect:/notas/" + estudianteId + "?materiaId=" + materiaId);
    }

}
