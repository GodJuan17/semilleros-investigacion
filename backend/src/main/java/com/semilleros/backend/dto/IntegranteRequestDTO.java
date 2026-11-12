package com.semilleros.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos de entrada para crear o actualizar un integrante")
public class IntegranteRequestDTO {

    @NotNull(message = "El ID del semillero es obligatorio")
    @Schema(description = "Identificador del semillero al que pertenece", example = "1")
    private Long semilleroId;

    @NotBlank(message = "El código del integrante es obligatorio")
    @Size(max = 40, message = "El código no puede superar los 40 caracteres")
    @Schema(description = "Código estudiantil o institucional del integrante", example = "2024102001")
    private String codigo;

    @NotBlank(message = "El nombre del integrante es obligatorio")
    @Size(max = 160, message = "El nombre no puede superar los 160 caracteres")
    @Schema(description = "Nombre completo del integrante", example = "Carlos Andrés Gómez")
    private String nombre;

    @NotBlank(message = "El programa académico es obligatorio")
    @Size(max = 160, message = "El programa no puede superar los 160 caracteres")
    @Schema(description = "Programa académico del integrante", example = "Ingeniería de Sistemas")
    private String programa;

    @NotNull(message = "Debe indicar si el integrante es líder")
    @Schema(description = "Indica si el integrante es líder del semillero", example = "false")
    private Boolean esLider;
}

