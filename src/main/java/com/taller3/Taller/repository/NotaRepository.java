package com.taller3.Taller.repository;

import com.taller3.Taller.model.Nota;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NotaRepository extends R2dbcRepository<Nota, Long> {
    Flux<Nota> findByMateriaId(Long materiaId);
    Flux<Nota> findByEstudianteId(Long estudianteId);
    Flux<Nota> findByMateriaIdAndEstudianteId(Long materiaId, Long estudianteId);
    
    Mono<Void> deleteByMateriaIdAndEstudianteId(Long materiaId, Long estudianteId);
    
    @Query("SELECT COALESCE(SUM(n.porcentaje), 0) FROM nota n WHERE n.materia_id = :materiaId AND n.estudiante_id = :estudianteId AND (:notaId IS NULL OR n.id <> :notaId)")
    Mono<Double> calcularPorcentaje(@Param("materiaId") Long materiaId, @Param("estudianteId") Long estudianteId, @Param("notaId") Long notaId);

    
    @Query("SELECT SUM(porcentaje) FROM nota WHERE materia_id = :materiaId AND estudiante_id = :estudianteId")
    Mono<Double> sumPorcentajesByMateriaAndEstudiante(Long materiaId, Long estudianteId);
}