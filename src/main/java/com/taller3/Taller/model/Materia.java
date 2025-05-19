package com.taller3.Taller.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Data;

@Data
@Table("materia")
public class Materia {
    @Id
    private Long id;
    private String nombre;
    private Integer creditos;
}