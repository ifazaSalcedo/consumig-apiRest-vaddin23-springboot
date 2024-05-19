package com.apirest.restdata;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AutorDTO {
    private Integer codigo;
    private String nombres;
    private String apellidos;
    private String telefono;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaActualizacion;
}
