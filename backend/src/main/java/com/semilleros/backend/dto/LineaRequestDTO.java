package com.semilleros.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos de entrada para crear o actualizar una línea de investigación")
public class LineaRequestDTO {

    @NotBlank(message = "El nombre de la línea es obligatorio")
    @Size(max = 160, message = "El nombre no puede superar los 160 caracteres")
    @Schema(description = "Nombre único de la línea de investigación", example = "Inteligencia Artificial")
    private String nombre;
}

