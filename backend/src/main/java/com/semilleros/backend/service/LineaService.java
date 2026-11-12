package com.semilleros.backend.service;

import com.semilleros.backend.dto.LineaRequestDTO;
import com.semilleros.backend.dto.LineaResponseDTO;
import com.semilleros.backend.entity.Linea;
import com.semilleros.backend.exception.BusinessException;
import com.semilleros.backend.exception.ResourceNotFoundException;
import com.semilleros.backend.repository.LineaRepository;
import com.semilleros.backend.repository.SemilleroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LineaService {

    private final LineaRepository lineaRepository;
    private final SemilleroRepository semilleroRepository;

    @Transactional(readOnly = true)
    public List<LineaResponseDTO> findAll() {
        return lineaRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public LineaResponseDTO findById(Long id) {
        return toResponseDTO(findEntityById(id));
    }

    @Transactional
    public LineaResponseDTO create(LineaRequestDTO request) {
        String nombre = request.getNombre().trim();
        if (lineaRepository.existsByNombreIgnoreCase(nombre)) {
            throw new BusinessException("Ya existe una línea registrada con el nombre: " + nombre);
        }

        Linea linea = Linea.builder()
                .nombre(nombre)
                .build();

        return toResponseDTO(lineaRepository.save(linea));
    }

    @Transactional
    public LineaResponseDTO update(Long id, LineaRequestDTO request) {
        Linea linea = findEntityById(id);
        String nombre = request.getNombre().trim();

        if (lineaRepository.existsByNombreIgnoreCaseAndIdNot(nombre, id)) {
            throw new BusinessException("Ya existe otra línea registrada con el nombre: " + nombre);
        }

        linea.setNombre(nombre);
        return toResponseDTO(lineaRepository.save(linea));
    }

    @Transactional
    public void delete(Long id) {
        Linea linea = findEntityById(id);
        if (semilleroRepository.existsByLineasId(id)) {
            throw new BusinessException("No se puede eliminar la línea porque está asociada a uno o más semilleros");
        }
        lineaRepository.delete(linea);
    }

    public Linea findEntityById(Long id) {
        return lineaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Línea no encontrada con id: " + id));
    }

    public LineaResponseDTO toResponseDTO(Linea linea) {
        return LineaResponseDTO.builder()
                .id(linea.getId())
                .nombre(linea.getNombre())
                .build();
    }
}

