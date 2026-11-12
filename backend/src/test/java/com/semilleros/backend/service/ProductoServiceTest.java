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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private SemilleroService semilleroService;

    @Mock
    private IntegranteService integranteService;

    @InjectMocks
    private ProductoService productoService;

    @Test
    void create_shouldSaveAndReturnProducto_whenValid() {
        Semillero semillero = Semillero.builder().id(10L).nombre("Semillero IA").sigla("SIAA").build();
        Integrante integrante = Integrante.builder()
                .id(1L)
                .semillero(semillero)
                .codigo("20231001")
                .nombre("Valentina Ruiz")
                .programa("Ingeniería de Sistemas")
                .esLider(true)
                .build();

        ProductoRequestDTO request = ProductoRequestDTO.builder()
                .semilleroId(10L)
                .nombre("Asistente Virtual RAG")
                .descripcion("Plataforma inteligente de apoyo académico.")
                .tipo(TipoProducto.SOFTWARE)
                .integranteIds(Set.of(1L))
                .build();

        Producto saved = Producto.builder()
                .id(100L)
                .semillero(semillero)
                .nombre("Asistente Virtual RAG")
                .descripcion("Plataforma inteligente de apoyo académico.")
                .tipo(TipoProducto.SOFTWARE)
                .integrantes(Set.of(integrante))
                .build();

        IntegranteResponseDTO integranteDTO = IntegranteResponseDTO.builder()
                .id(1L)
                .semilleroId(10L)
                .nombre("Valentina Ruiz")
                .codigo("20231001")
                .build();

        when(semilleroService.findEntityById(10L)).thenReturn(semillero);
        when(productoRepository.existsBySemilleroIdAndNombreIgnoreCase(10L, "Asistente Virtual RAG")).thenReturn(false);
        when(integranteService.findEntityById(1L)).thenReturn(integrante);
        when(productoRepository.save(any(Producto.class))).thenReturn(saved);
        when(integranteService.toResponseDTO(integrante)).thenReturn(integranteDTO);

        ProductoResponseDTO result = productoService.create(request);

        assertEquals(100L, result.getId());
        assertEquals(10L, result.getSemilleroId());
        assertEquals("Asistente Virtual RAG", result.getNombre());
        assertEquals(TipoProducto.SOFTWARE, result.getTipo());
        assertEquals("Software", result.getTipoNombre());
        assertEquals("Desarrollo Tecnológico e Innovación", result.getTipoCategoria());
        assertEquals(1, result.getIntegrantes().size());
    }

    @Test
    void create_shouldThrowBusinessException_whenNombreDuplicatedInSemillero() {
        Semillero semillero = Semillero.builder().id(10L).nombre("Semillero IA").sigla("SIAA").build();
        ProductoRequestDTO request = ProductoRequestDTO.builder()
                .semilleroId(10L)
                .nombre("Asistente Virtual RAG")
                .descripcion("Descripción")
                .tipo(TipoProducto.SOFTWARE)
                .integranteIds(Set.of(1L))
                .build();

        when(semilleroService.findEntityById(10L)).thenReturn(semillero);
        when(productoRepository.existsBySemilleroIdAndNombreIgnoreCase(10L, "Asistente Virtual RAG")).thenReturn(true);

        assertThrows(BusinessException.class, () -> productoService.create(request));
    }

    @Test
    void create_shouldThrowBusinessException_whenIntegranteBelongsToAnotherSemillero() {
        Semillero semilleroObjetivo = Semillero.builder().id(10L).nombre("Semillero IA").sigla("SIAA").build();
        Semillero otroSemillero = Semillero.builder().id(20L).nombre("Otro Semillero").sigla("OS").build();
        Integrante integranteExterno = Integrante.builder()
                .id(2L)
                .semillero(otroSemillero)
                .nombre("Carlos Mora")
                .build();

        ProductoRequestDTO request = ProductoRequestDTO.builder()
                .semilleroId(10L)
                .nombre("Artículo Científico")
                .descripcion("Estudio experimental.")
                .tipo(TipoProducto.ARTICULO_INVESTIGACION)
                .integranteIds(Set.of(2L))
                .build();

        when(semilleroService.findEntityById(10L)).thenReturn(semilleroObjetivo);
        when(productoRepository.existsBySemilleroIdAndNombreIgnoreCase(10L, "Artículo Científico")).thenReturn(false);
        when(integranteService.findEntityById(2L)).thenReturn(integranteExterno);

        assertThrows(BusinessException.class, () -> productoService.create(request));
    }

    @Test
    void findById_shouldThrowResourceNotFoundException_whenNotExists() {
        when(productoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productoService.findById(999L));
    }

    @Test
    void delete_shouldRemoveProducto_whenExists() {
        Semillero semillero = Semillero.builder().id(10L).nombre("Semillero IA").sigla("SIAA").build();
        Producto producto = Producto.builder()
                .id(5L)
                .semillero(semillero)
                .nombre("Prototipo IoT")
                .descripcion("Sensor climático")
                .tipo(TipoProducto.PROTOTIPO_INDUSTRIAL)
                .build();

        when(productoRepository.findById(5L)).thenReturn(Optional.of(producto));

        productoService.delete(5L);

        verify(productoRepository).delete(producto);
    }

    @Test
    void getTiposProducto_shouldReturnMincienciasTypology() {
        List<TipoProductoDTO> tipos = productoService.getTiposProducto();

        assertFalse(tipos.isEmpty());
        assertEquals(TipoProducto.values().length, tipos.size());
    }
}

