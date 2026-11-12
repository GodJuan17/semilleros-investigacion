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
@Schema(description = "Datos de respuesta de un integrante")
public class IntegranteResponseDTO {

    @Schema(description = "Identificador único del integrante", example = "1")
    private Long id;

    @Schema(description = "ID del semillero al que pertenece", example = "1")
    private Long semilleroId;

    @Schema(description = "Nombre del semillero", example = "Semillero de Inteligencia Artificial Aplicada")
    private String semilleroNombre;

    @Schema(description = "Sigla del semillero", example = "SIAA")
    private String semilleroSigla;

    @Schema(description = "Código del integrante", example = "2024102001")
    private String codigo;

    @Schema(description = "Nombre completo del integrante", example = "Carlos Andrés Gómez")
    private String nombre;

    @Schema(description = "Programa académico", example = "Ingeniería de Sistemas")
    private String programa;

    @Schema(description = "Indica si es líder del semillero", example = "false")
    private Boolean esLider;
}

