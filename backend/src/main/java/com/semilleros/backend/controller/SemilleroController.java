package com.semilleros.backend.controller;

import com.semilleros.backend.dto.SemilleroRequestDTO;
import com.semilleros.backend.dto.SemilleroResponseDTO;
import com.semilleros.backend.service.SemilleroService;
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
@RequestMapping("/api/semilleros")
@RequiredArgsConstructor
@Tag(name = "Semilleros", description = "Operaciones CRUD para la gestión de semilleros de investigación")
public class SemilleroController {

    private final SemilleroService semilleroService;

    @GetMapping
    @Operation(summary = "Listar semilleros", description = "Retorna todos los semilleros registrados")
    public ResponseEntity<List<SemilleroResponseDTO>> findAll() {
        return ResponseEntity.ok(semilleroService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener semillero por ID", description = "Retorna un semillero según su identificador")
    public ResponseEntity<SemilleroResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(semilleroService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear semillero", description = "Registra un nuevo semillero validando sigla única y existencia del director")
    public ResponseEntity<SemilleroResponseDTO> create(@Valid @RequestBody SemilleroRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(semilleroService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar semillero", description = "Actualiza los datos de un semillero existente")
    public ResponseEntity<SemilleroResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody SemilleroRequestDTO request) {
        return ResponseEntity.ok(semilleroService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar semillero", description = "Elimina un semillero por su identificador")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        semilleroService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{semilleroId}/lineas/{lineaId}")
    @Operation(summary = "Asociar línea a semillero", description = "Asocia una línea de investigación existente a un semillero")
    public ResponseEntity<SemilleroResponseDTO> addLinea(
            @PathVariable Long semilleroId,
            @PathVariable Long lineaId) {
        return ResponseEntity.ok(semilleroService.addLinea(semilleroId, lineaId));
    }

    @DeleteMapping("/{semilleroId}/lineas/{lineaId}")
    @Operation(summary = "Desasociar línea de semillero", description = "Remueve la asociación entre un semillero y una línea de investigación")
    public ResponseEntity<SemilleroResponseDTO> removeLinea(
            @PathVariable Long semilleroId,
            @PathVariable Long lineaId) {
        return ResponseEntity.ok(semilleroService.removeLinea(semilleroId, lineaId));
    }
}

