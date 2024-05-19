package com.apirest.restdata;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LibroDTO {
    private Integer codigo;
    private String isbn;
    private String nombre;
    private String descripcionGeneral;
    private String editorial;
    private String genero;
    private int numeroPaginas;
    private BigDecimal precio;
    private LocalDate fechaEdicion;
    private AutorDTO autorDTO;
    private Integer idAutor;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaActualizacion;
}
