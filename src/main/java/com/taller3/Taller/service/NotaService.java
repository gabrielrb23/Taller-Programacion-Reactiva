package com.taller3.Taller.service;

import com.taller3.Taller.model.Nota;
import com.taller3.Taller.repository.NotaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class NotaService {
    private final NotaRepository notaRepository;

    public Mono<Nota> saveNota(Nota nota) {
        if (nota.getValor() < 0.0 || nota.getValor() > 5.0) {
            return Mono.error(new RuntimeException("La nota debe estar entre 0.0 y 5.0"));
        }
        if (nota.getPorcentaje() <= 0.0 || nota.getPorcentaje() > 100.0) {
            return Mono.error(new RuntimeException("El porcentaje debe estar entre 0 y 100"));
        }

        Mono<Double> sumaPorcentajesMono;
        if (nota.getId() != null) {
            sumaPorcentajesMono = notaRepository.calcularPorcentaje(
                nota.getMateriaId(), nota.getEstudianteId(), nota.getId());
        } else {
            sumaPorcentajesMono = notaRepository.sumPorcentajesByMateriaAndEstudiante(
                nota.getMateriaId(), nota.getEstudianteId());
        }

        return sumaPorcentajesMono.flatMap(totalPorcentaje -> {
            double nuevoTotal = (totalPorcentaje != null ? totalPorcentaje : 0.0) + nota.getPorcentaje();
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
                
                return sumaPonderada;
            });
    }

    public Flux<Nota> findByMateriaIdAndEstudianteId(Long materiaId, Long estudianteId) {
        return notaRepository.findByMateriaIdAndEstudianteId(materiaId, estudianteId);
    }

    public Mono<Void> deleteByMateriaIdAndEstudianteId(Long materiaId, Long estudianteId) {
        return notaRepository.deleteByMateriaIdAndEstudianteId(materiaId, estudianteId);
    }

    public Mono<Void> deleteById(Long id) {
        return notaRepository.deleteById(id);
    }

    public Mono<Nota> findById(Long id) {
        return notaRepository.findById(id);
    }
    
}