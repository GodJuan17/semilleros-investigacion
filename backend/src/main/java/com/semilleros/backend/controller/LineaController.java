package com.semilleros.backend.controller;

import com.semilleros.backend.dto.LineaRequestDTO;
import com.semilleros.backend.dto.LineaResponseDTO;
import com.semilleros.backend.service.LineaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/lineas")
@RequiredArgsConstructor
@Tag(name = "Líneas", description = "Operaciones CRUD para la gestión de líneas de investigación")
public class LineaController {

    private final LineaService lineaService;

    @GetMapping
    @Operation(summary = "Listar líneas", description = "Retorna todas las líneas de investigación registradas")
    public ResponseEntity<List<LineaResponseDTO>> findAll() {
        return ResponseEntity.ok(lineaService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener línea por ID", description = "Retorna una línea de investigación según su identificador")
    public ResponseEntity<LineaResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(lineaService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear línea", description = "Registra una nueva línea de investigación validando que el nombre sea único")
    public ResponseEntity<LineaResponseDTO> create(@Valid @RequestBody LineaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(lineaService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar línea", description = "Actualiza el nombre de una línea de investigación existente")
    public ResponseEntity<LineaResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody LineaRequestDTO request) {
        return ResponseEntity.ok(lineaService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar línea", description = "Elimina una línea si no está asociada a ningún semillero")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        lineaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

