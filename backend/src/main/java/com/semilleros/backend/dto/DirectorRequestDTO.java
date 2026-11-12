package com.semilleros.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
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
@Schema(description = "Datos de entrada para crear o actualizar un director")
public class DirectorRequestDTO {

    @NotBlank(message = "El nombre del director es obligatorio")
    @Size(max = 160, message = "El nombre no puede superar los 160 caracteres")
    @Schema(description = "Nombre completo del director", example = "Dra. Ana María Pérez")
    private String nombre;

    @NotBlank(message = "El correo del director es obligatorio")
    @Email(message = "El formato del correo electrónico no es válido")
    @Size(max = 180, message = "El correo no puede superar los 180 caracteres")
    @Schema(description = "Correo electrónico institucional del director", example = "ana.perez@universidad.edu.co")
    private String correo;
}

