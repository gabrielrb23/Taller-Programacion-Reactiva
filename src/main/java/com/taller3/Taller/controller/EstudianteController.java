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

    @GetMapping("/materias/{materiaId}/estudiantes/nuevo")
    public Mono<Rendering> mostrarFormularioNuevoEstudianteConMateria(@PathVariable Long materiaId) {
        return Mono.just(Rendering.view("editar-estudiante")
            .modelAttribute("estudiante", new Estudiante())
            .modelAttribute("modo", "crear")
            .modelAttribute("materiaId", materiaId)
            .build());
    }

    @PostMapping("/estudiantes/guardar")
    public Mono<Rendering> guardarEstudiante(
            @ModelAttribute Estudiante estudiante,
            @RequestParam(value = "materiaId", required = false) Long materiaId) {
        
        System.out.println("Materia ID recibido: " + materiaId); // Esto imprimirá en la consola
        
        return estudianteService.save(estudiante)
            .then(Mono.defer(() -> {
                if (materiaId != null) {
                    return Mono.just(Rendering.redirectTo("/materias/" + materiaId + "/estudiantes").build());
                } else {
                    return Mono.just(Rendering.redirectTo("/estudiantes/view").build());
                }
            }))
            .onErrorResume(e -> {
                System.err.println("Error al guardar estudiante: " + e.getMessage());
                e.printStackTrace(); // Imprime el stack trace para depuración
                if (materiaId != null) {
                    return Mono.just(Rendering.redirectTo("/materias/" + materiaId + "/estudiantes?error=save_error").build());
                } else {
                    return Mono.just(Rendering.redirectTo("/estudiantes/view?error=save_error").build());
                }
            });
    }

    @GetMapping("/estudiantes/editar/{id}")
    public Mono<Rendering> editarEstudiante(@PathVariable Long id) {
        return estudianteService.findById(id)
            .map(est -> Rendering.view("editar-estudiante")
                .modelAttribute("estudiante", est)
                .modelAttribute("modo", "editar")
                .build());
    }

    @PostMapping("/estudiantes/actualizar")
    public Mono<Rendering> actualizarEstudiante(
            @ModelAttribute Estudiante estudiante,
            @RequestParam(value = "materiaId", required = false) Long materiaId) {
        
        return estudianteService.save(estudiante)
            .then(Mono.defer(() -> {
                if (materiaId != null) {
                    // Si hay una materiaId, redirige a la lista de estudiantes de esa materia
                    return Mono.just(Rendering.redirectTo("/materias/" + materiaId + "/estudiantes").build());
                } else {
                    // Si no hay materiaId, usa el comportamiento anterior
                    return Mono.just(Rendering.redirectTo("/estudiantes/view").build());
                }
            }));
    }

    @GetMapping("/estudiantes/eliminar/{id}")
    public Mono<Rendering> eliminarEstudiante(@PathVariable Long id) {
        return estudianteService.deleteById(id)
            .then(Mono.just(Rendering.redirectTo("/estudiantes/view").build()));
    }

    
}
