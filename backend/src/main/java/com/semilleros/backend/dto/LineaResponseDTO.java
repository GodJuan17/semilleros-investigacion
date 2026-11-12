package com.semilleros.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos de respuesta de una línea de investigación")
public class LineaResponseDTO {

    @Schema(description = "Identificador único de la línea", example = "1")
    private Long id;

    @Schema(description = "Nombre de la línea de investigación", example = "Inteligencia Artificial")
    private String nombre;
}

