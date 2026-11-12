package com.semilleros.backend.controller;

import com.semilleros.backend.dto.ProductoRequestDTO;
import com.semilleros.backend.dto.ProductoResponseDTO;
import com.semilleros.backend.dto.TipoProductoDTO;
import com.semilleros.backend.service.ProductoService;
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
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Operaciones CRUD para la gestión de productos de semilleros según tipología Minciencias")
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    @Operation(summary = "Listar productos", description = "Retorna todos los productos desarrollados por los semilleros")
    public ResponseEntity<List<ProductoResponseDTO>> findAll() {
        return ResponseEntity.ok(productoService.findAll());
    }

    @GetMapping("/tipos")
    @Operation(summary = "Listar tipos de producto Minciencias", description = "Retorna el catálogo de tipos de producto según la tipología de Minciencias")
    public ResponseEntity<List<TipoProductoDTO>> getTiposProducto() {
        return ResponseEntity.ok(productoService.getTiposProducto());
    }

    @GetMapping("/semillero/{semilleroId}")
    @Operation(summary = "Listar productos por semillero", description = "Retorna los productos desarrollados por un semillero específico")
    public ResponseEntity<List<ProductoResponseDTO>> findBySemilleroId(@PathVariable Long semilleroId) {
        return ResponseEntity.ok(productoService.findBySemilleroId(semilleroId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID", description = "Retorna un producto según su identificador")
    public ResponseEntity<ProductoResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear producto", description = "Registra un nuevo producto desarrollado por un semillero y asocia los integrantes participantes")
    public ResponseEntity<ProductoResponseDTO> create(@Valid @RequestBody ProductoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto", description = "Actualiza los datos y los integrantes participantes de un producto existente")
    public ResponseEntity<ProductoResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequestDTO request) {
        return ResponseEntity.ok(productoService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto", description = "Elimina un producto por su identificador")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

