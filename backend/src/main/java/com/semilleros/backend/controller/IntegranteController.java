package com.semilleros.backend.controller;

import com.semilleros.backend.dto.IntegranteRequestDTO;
import com.semilleros.backend.dto.IntegranteResponseDTO;
import com.semilleros.backend.service.IntegranteService;
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
@RequestMapping("/api/integrantes")
@RequiredArgsConstructor
@Tag(name = "Integrantes", description = "Operaciones CRUD para la gestión de integrantes de semilleros")
public class IntegranteController {

    private final IntegranteService integranteService;

    @GetMapping
    @Operation(summary = "Listar integrantes", description = "Retorna todos los integrantes registrados")
    public ResponseEntity<List<IntegranteResponseDTO>> findAll() {
        return ResponseEntity.ok(integranteService.findAll());
    }

    @GetMapping("/semillero/{semilleroId}")
    @Operation(summary = "Listar integrantes por semillero", description = "Retorna los integrantes asociados a un semillero específico")
    public ResponseEntity<List<IntegranteResponseDTO>> findBySemilleroId(@PathVariable Long semilleroId) {
        return ResponseEntity.ok(integranteService.findBySemilleroId(semilleroId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener integrante por ID", description = "Retorna un integrante según su identificador")
    public ResponseEntity<IntegranteResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(integranteService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear integrante", description = "Registra un nuevo integrante asociado a un semillero")
    public ResponseEntity<IntegranteResponseDTO> create(@Valid @RequestBody IntegranteRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(integranteService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar integrante", description = "Actualiza los datos de un integrante existente")
    public ResponseEntity<IntegranteResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody IntegranteRequestDTO request) {
        return ResponseEntity.ok(integranteService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar integrante", description = "Elimina un integrante por su identificador")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        integranteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

