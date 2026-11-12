package com.semilleros.backend.service;

import com.semilleros.backend.dto.IntegranteResponseDTO;
import com.semilleros.backend.dto.ProductoRequestDTO;
import com.semilleros.backend.dto.ProductoResponseDTO;
import com.semilleros.backend.dto.TipoProductoDTO;
import com.semilleros.backend.entity.Integrante;
import com.semilleros.backend.entity.Producto;
import com.semilleros.backend.entity.Semillero;
import com.semilleros.backend.entity.TipoProducto;
import com.semilleros.backend.exception.BusinessException;
import com.semilleros.backend.exception.ResourceNotFoundException;
import com.semilleros.backend.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final SemilleroService semilleroService;
    private final IntegranteService integranteService;

    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> findAll() {
        return productoRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> findBySemilleroId(Long semilleroId) {
        semilleroService.findEntityById(semilleroId);
        return productoRepository.findBySemilleroId(semilleroId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductoResponseDTO findById(Long id) {
        return toResponseDTO(findEntityById(id));
    }

    public List<TipoProductoDTO> getTiposProducto() {
        return Arrays.stream(TipoProducto.values())
                .map(t -> TipoProductoDTO.builder()
                        .codigo(t.name())
                        .nombre(t.getNombre())
                        .categoria(t.getCategoria())
                        .build())
                .toList();
    }

    @Transactional
    public ProductoResponseDTO create(ProductoRequestDTO request) {
        Semillero semillero = semilleroService.findEntityById(request.getSemilleroId());
        String nombre = request.getNombre().trim();

        if (productoRepository.existsBySemilleroIdAndNombreIgnoreCase(semillero.getId(), nombre)) {
            throw new BusinessException("Ya existe un producto con el nombre '" + nombre + "' en este semillero");
        }

        Set<Integrante> integrantes = resolveIntegrantes(semillero.getId(), request.getIntegranteIds());

        Producto producto = Producto.builder()
                .semillero(semillero)
                .nombre(nombre)
                .descripcion(request.getDescripcion().trim())
                .tipo(request.getTipo())
                .integrantes(integrantes)
                .build();

        return toResponseDTO(productoRepository.save(producto));
    }

    @Transactional
    public ProductoResponseDTO update(Long id, ProductoRequestDTO request) {
        Producto producto = findEntityById(id);
        Semillero semillero = semilleroService.findEntityById(request.getSemilleroId());
        String nombre = request.getNombre().trim();

        if (productoRepository.existsBySemilleroIdAndNombreIgnoreCaseAndIdNot(semillero.getId(), nombre, id)) {
            throw new BusinessException("Ya existe otro producto con el nombre '" + nombre + "' en este semillero");
        }

        Set<Integrante> integrantes = resolveIntegrantes(semillero.getId(), request.getIntegranteIds());

        producto.setSemillero(semillero);
        producto.setNombre(nombre);
        producto.setDescripcion(request.getDescripcion().trim());
        producto.setTipo(request.getTipo());
        producto.setIntegrantes(integrantes);

        return toResponseDTO(productoRepository.save(producto));
    }

    @Transactional
    public void delete(Long id) {
        Producto producto = findEntityById(id);
        producto.getIntegrantes().clear();
        productoRepository.delete(producto);
    }

    private Producto findEntityById(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
    }

    private Set<Integrante> resolveIntegrantes(Long semilleroId, Set<Long> integranteIds) {
        Set<Integrante> integrantes = new HashSet<>();
        if (integranteIds != null) {
            for (Long integranteId : integranteIds) {
                Integrante integrante = integranteService.findEntityById(integranteId);
                if (!integrante.getSemillero().getId().equals(semilleroId)) {
                    throw new BusinessException(
                            "El integrante '" + integrante.getNombre() + "' no pertenece al semillero seleccionado"
                    );
                }
                integrantes.add(integrante);
            }
        }
        return integrantes;
    }

    private ProductoResponseDTO toResponseDTO(Producto producto) {
        Semillero semillero = producto.getSemillero();
        List<IntegranteResponseDTO> integrantesDTO = producto.getIntegrantes().stream()
                .sorted(Comparator.comparing(Integrante::getId))
                .map(integranteService::toResponseDTO)
                .toList();

        return ProductoResponseDTO.builder()
                .id(producto.getId())
                .semilleroId(semillero.getId())
                .semilleroNombre(semillero.getNombre())
                .semilleroSigla(semillero.getSigla())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .tipo(producto.getTipo())
                .tipoNombre(producto.getTipo().getNombre())
                .tipoCategoria(producto.getTipo().getCategoria())
                .integrantes(integrantesDTO)
                .build();
    }
}

