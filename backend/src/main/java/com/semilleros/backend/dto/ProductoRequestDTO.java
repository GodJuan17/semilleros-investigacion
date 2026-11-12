package com.semilleros.backend.dto;

import com.semilleros.backend.entity.TipoProducto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos de entrada para crear o actualizar un producto de investigación")
public class ProductoRequestDTO {

    @NotNull(message = "El ID del semillero es obligatorio")
    @Schema(description = "Identificador del semillero al que pertenece el producto", example = "1")
    private Long semilleroId;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 200, message = "El nombre del producto no puede superar los 200 caracteres")
    @Schema(description = "Nombre del producto desarrollado", example = "Asistente Virtual Académico con RAG")
    private String nombre;

    @NotBlank(message = "La descripción del producto es obligatoria")
    @Schema(description = "Descripción detallada del producto y su alcance", example = "Plataforma web basada en IA para orientar a estudiantes.")
    private String descripcion;

    @NotNull(message = "El tipo de producto según Minciencias es obligatorio")
    @Schema(description = "Tipo de producto según la tipología de Minciencias", example = "SOFTWARE")
    private TipoProducto tipo;

    @NotEmpty(message = "Debe asociar al menos un integrante del semillero al producto")
    @Schema(description = "IDs de los integrantes del semillero que participaron en el producto", example = "[1, 2]")
    private Set<Long> integranteIds;
}

