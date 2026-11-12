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
@Schema(description = "Tipo de producto según la tipología de Minciencias")
public class TipoProductoDTO {

    @Schema(description = "Código identificador del tipo de producto", example = "SOFTWARE")
    private String codigo;

    @Schema(description = "Nombre legible del tipo de producto", example = "Software")
    private String nombre;

    @Schema(description = "Categoría principal de Minciencias", example = "Desarrollo Tecnológico e Innovación")
    private String categoria;
}

