package com.semilleros.backend.service;

import com.semilleros.backend.dto.LineaRequestDTO;
import com.semilleros.backend.dto.LineaResponseDTO;
import com.semilleros.backend.entity.Linea;
import com.semilleros.backend.exception.BusinessException;
import com.semilleros.backend.exception.ResourceNotFoundException;
import com.semilleros.backend.repository.LineaRepository;
import com.semilleros.backend.repository.SemilleroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LineaServiceTest {

    @Mock
    private LineaRepository lineaRepository;

    @Mock
    private SemilleroRepository semilleroRepository;

    @InjectMocks
    private LineaService lineaService;

    @Test
    void create_shouldSaveAndReturnLinea_whenNombreIsUnique() {
        LineaRequestDTO request = new LineaRequestDTO("Inteligencia Artificial");
        Linea saved = Linea.builder().id(1L).nombre("Inteligencia Artificial").build();

        when(lineaRepository.existsByNombreIgnoreCase("Inteligencia Artificial")).thenReturn(false);
        when(lineaRepository.save(any(Linea.class))).thenReturn(saved);

        LineaResponseDTO result = lineaService.create(request);

        assertEquals(1L, result.getId());
        assertEquals("Inteligencia Artificial", result.getNombre());
    }

    @Test
    void create_shouldThrowBusinessException_whenNombreAlreadyExists() {
        LineaRequestDTO request = new LineaRequestDTO("Inteligencia Artificial");
        when(lineaRepository.existsByNombreIgnoreCase("Inteligencia Artificial")).thenReturn(true);

        assertThrows(BusinessException.class, () -> lineaService.create(request));
    }

    @Test
    void delete_shouldThrowBusinessException_whenLineaAssociatedWithSemillero() {
        Linea linea = Linea.builder().id(1L).nombre("IA").build();
        when(lineaRepository.findById(1L)).thenReturn(Optional.of(linea));
        when(semilleroRepository.existsByLineasId(1L)).thenReturn(true);

        assertThrows(BusinessException.class, () -> lineaService.delete(1L));
    }

    @Test
    void findById_shouldThrowResourceNotFound_whenMissing() {
        when(lineaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> lineaService.findById(99L));
    }

    @Test
    void delete_shouldDeleteLinea_whenNotAssociated() {
        Linea linea = Linea.builder().id(1L).nombre("IA").build();
        when(lineaRepository.findById(1L)).thenReturn(Optional.of(linea));
        when(semilleroRepository.existsByLineasId(1L)).thenReturn(false);

        lineaService.delete(1L);

        verify(lineaRepository).delete(linea);
    }
}

