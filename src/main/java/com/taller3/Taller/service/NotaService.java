package com.taller3.Taller.service;

import com.taller3.Taller.model.Nota;
import com.taller3.Taller.repository.NotaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class NotaService {
    private final NotaRepository notaRepository;

    public Mono<Nota> saveNota(Nota nota) {
        // Validar rango de la nota (0.0 - 5.0)
        if (nota.getValor() < 0.0 || nota.getValor() > 5.0) {
            return Mono.error(new RuntimeException("La nota debe estar entre 0.0 y 5.0"));
        }
        
        // Validar porcentaje positivo
        if (nota.getPorcentaje() <= 0.0) {
            return Mono.error(new RuntimeException("El porcentaje debe ser mayor a 0"));
        }

        return notaRepository.sumPorcentajesByMateriaAndEstudiante(nota.getMateriaId(), nota.getEstudianteId())
            .flatMap(totalPorcentaje -> {
                double nuevoTotal = totalPorcentaje != null ? totalPorcentaje + nota.getPorcentaje() : nota.getPorcentaje();
                if (nuevoTotal > 100.0) {
                    return Mono.error(new RuntimeException("La suma de porcentajes supera el 100%"));
                }
                return notaRepository.save(nota);
            });
    }

    public Mono<Double> calcularPromedio(Long estudianteId, Long materiaId) {
        return notaRepository.findByMateriaIdAndEstudianteId(materiaId, estudianteId)
            .collectList()
            .map(notas -> {
                if (notas.isEmpty()) return 0.0;
                
                double sumaPonderada = 0.0;
                double sumaPorcentajes = 0.0;
                
                for (Nota nota : notas) {
                    sumaPonderada += nota.getValor() * (nota.getPorcentaje() / 100);
                    sumaPorcentajes += nota.getPorcentaje();
                }
                
                // Validar que los porcentajes sumen exactamente 100%
                if (Math.abs(sumaPorcentajes - 100.0) > 0.01) {
                    throw new RuntimeException("Los porcentajes no suman 100%");
                }
                
                return sumaPonderada;
            });
    }
}