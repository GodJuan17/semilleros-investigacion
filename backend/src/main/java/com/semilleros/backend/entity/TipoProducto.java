package com.semilleros.backend.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Tipología oficial de productos de investigación, desarrollo tecnológico e innovación según Minciencias.
 */
@Getter
@RequiredArgsConstructor
public enum TipoProducto {

    // Generación de Nuevo Conocimiento
    ARTICULO_INVESTIGACION("Artículo de investigación", "Generación de Nuevo Conocimiento"),
    LIBRO_INVESTIGACION("Libro resultado de investigación", "Generación de Nuevo Conocimiento"),
    CAPITULO_LIBRO("Capítulo en libro resultado de investigación", "Generación de Nuevo Conocimiento"),
    PATENTE_O_REGISTRO("Producto tecnológico patentado o registrado", "Generación de Nuevo Conocimiento"),

    // Desarrollo Tecnológico e Innovación
    SOFTWARE("Software", "Desarrollo Tecnológico e Innovación"),
    PROTOTIPO_INDUSTRIAL("Prototipo industrial / Planta piloto", "Desarrollo Tecnológico e Innovación"),
    INNOVACION_EMPRESARIAL("Innovación empresarial o en gestión", "Desarrollo Tecnológico e Innovación"),
    CONSULTORIA_CIENTIFICA("Consultoría científico-tecnológica", "Desarrollo Tecnológico e Innovación"),

    // Apropiación Social del Conocimiento
    EVENTO_CIENTIFICO("Evento científico (Ponencia / Póster)", "Apropiación Social del Conocimiento"),
    DOCUMENTO_TRABAJO("Documento de trabajo (Working paper)", "Apropiación Social del Conocimiento"),
    CONTENIDO_DIGITAL("Producción de contenido digital", "Apropiación Social del Conocimiento"),
    ESTRATEGIA_PEDAGOGICA("Estrategia pedagógica para el fomento a la CTeI", "Apropiación Social del Conocimiento"),

    // Formación de Recurso Humano
    TRABAJO_GRADO_PREGRADO("Trabajo de grado de pregrado", "Formación de Recurso Humano"),
    TRABAJO_GRADO_POSGRADO("Trabajo de grado de maestría / doctorado", "Formación de Recurso Humano"),
    PROYECTO_IDI_FORMACION("Proyecto de I+D+i con formación", "Formación de Recurso Humano");

    private final String nombre;
    private final String categoria;
}

