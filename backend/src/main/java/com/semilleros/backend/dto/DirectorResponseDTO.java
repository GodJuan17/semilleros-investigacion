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
@Schema(description = "Datos de respuesta de un director")
public class DirectorResponseDTO {

    @Schema(description = "Identificador único del director", example = "1")
    private Long id;

    @Schema(description = "Nombre completo del director", example = "Dra. Ana María Pérez")
    private String nombre;

    @Schema(description = "Correo electrónico del director", example = "ana.perez@universidad.edu.co")
    private String correo;
}

