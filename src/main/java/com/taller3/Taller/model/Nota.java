package com.taller3.Taller.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Data;

@Data
@Table("nota")
public class Nota {

    @Id
    private Long id;

    private Long materiaId;
    private Long estudianteId;
    private String observacion;
    private Double valor;
    private Double porcentaje;
}