package com.semilleros.backend.service;

import com.semilleros.backend.dto.DirectorRequestDTO;
import com.semilleros.backend.dto.DirectorResponseDTO;
import com.semilleros.backend.entity.Director;
import com.semilleros.backend.exception.BusinessException;
import com.semilleros.backend.exception.ResourceNotFoundException;
import com.semilleros.backend.repository.DirectorRepository;
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
class DirectorServiceTest {

    @Mock
    private DirectorRepository directorRepository;

    @Mock
    private SemilleroRepository semilleroRepository;

    @InjectMocks
    private DirectorService directorService;

    @Test
    void create_shouldSaveAndReturnDirector_whenCorreoIsUnique() {
        DirectorRequestDTO request = new DirectorRequestDTO("Ana Pérez", "ana@uni.edu");
        Director saved = Director.builder().id(1L).nombre("Ana Pérez").correo("ana@uni.edu").build();

        when(directorRepository.existsByCorreoIgnoreCase("ana@uni.edu")).thenReturn(false);
        when(directorRepository.save(any(Director.class))).thenReturn(saved);

        DirectorResponseDTO result = directorService.create(request);

        assertEquals(1L, result.getId());
        assertEquals("Ana Pérez", result.getNombre());
        assertEquals("ana@uni.edu", result.getCorreo());
    }

    @Test
    void create_shouldThrowBusinessException_whenCorreoAlreadyExists() {
        DirectorRequestDTO request = new DirectorRequestDTO("Ana Pérez", "ana@uni.edu");
        when(directorRepository.existsByCorreoIgnoreCase("ana@uni.edu")).thenReturn(true);

        assertThrows(BusinessException.class, () -> directorService.create(request));
    }

    @Test
    void delete_shouldThrowBusinessException_whenDirectorHasSemilleros() {
        Director director = Director.builder().id(1L).nombre("Ana").correo("ana@uni.edu").build();
        when(directorRepository.findById(1L)).thenReturn(Optional.of(director));
        when(semilleroRepository.existsByDirectorId(1L)).thenReturn(true);

        assertThrows(BusinessException.class, () -> directorService.delete(1L));
    }

    @Test
    void findById_shouldThrowResourceNotFound_whenMissing() {
        when(directorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> directorService.findById(99L));
    }

    @Test
    void delete_shouldDeleteDirector_whenNoSemillerosAssociated() {
        Director director = Director.builder().id(1L).nombre("Ana").correo("ana@uni.edu").build();
        when(directorRepository.findById(1L)).thenReturn(Optional.of(director));
        when(semilleroRepository.existsByDirectorId(1L)).thenReturn(false);

        directorService.delete(1L);

        verify(directorRepository).delete(director);
    }
}

