package com.semilleros.backend.controller;

import com.semilleros.backend.dto.DirectorRequestDTO;
import com.semilleros.backend.dto.DirectorResponseDTO;
import com.semilleros.backend.service.DirectorService;
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
@RequestMapping("/api/directores")
@RequiredArgsConstructor
@Tag(name = "Directores", description = "Operaciones CRUD para la gestión de directores")
public class DirectorController {

    private final DirectorService directorService;

    @GetMapping
    @Operation(summary = "Listar directores", description = "Retorna todos los directores registrados")
    public ResponseEntity<List<DirectorResponseDTO>> findAll() {
        return ResponseEntity.ok(directorService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener director por ID", description = "Retorna un director según su identificador")
    public ResponseEntity<DirectorResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(directorService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear director", description = "Registra un nuevo director validando que el correo sea único")
    public ResponseEntity<DirectorResponseDTO> create(@Valid @RequestBody DirectorRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(directorService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar director", description = "Actualiza los datos de un director existente")
    public ResponseEntity<DirectorResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody DirectorRequestDTO request) {
        return ResponseEntity.ok(directorService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar director", description = "Elimina un director si no tiene semilleros asociados")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        directorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

