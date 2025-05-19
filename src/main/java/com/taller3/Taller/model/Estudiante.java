package com.taller3.Taller.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Data;

@Data
@Table("estudiante")
public class Estudiante {
    @Id
    private Long id;
    private String nombre;
    private String apellido;
    private String correo;
}