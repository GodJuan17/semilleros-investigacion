package com.semilleros.backend.dto;

import com.semilleros.backend.entity.TipoProducto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos de respuesta de un producto desarrollado por un semillero")
public class ProductoResponseDTO {

    @Schema(description = "Identificador único del producto", example = "1")
    private Long id;

    @Schema(description = "ID del semillero al que pertenece", example = "1")
    private Long semilleroId;

    @Schema(description = "Nombre del semillero", example = "Semillero de Inteligencia Artificial Aplicada")
    private String semilleroNombre;

    @Schema(description = "Sigla del semillero", example = "SIAA")
    private String semilleroSigla;

    @Schema(description = "Nombre del producto", example = "Asistente Virtual Académico con RAG")
    private String nombre;

    @Schema(description = "Descripción del producto", example = "Plataforma web basada en IA para orientar a estudiantes.")
    private String descripcion;

    @Schema(description = "Código del tipo de producto Minciencias", example = "SOFTWARE")
    private TipoProducto tipo;

    @Schema(description = "Nombre legible del tipo de producto Minciencias", example = "Software")
    private String tipoNombre;

    @Schema(description = "Categoría Minciencias del producto", example = "Desarrollo Tecnológico e Innovación")
    private String tipoCategoria;

    @Schema(description = "Integrantes del semillero que participaron en el producto")
    private List<IntegranteResponseDTO> integrantes;
}

