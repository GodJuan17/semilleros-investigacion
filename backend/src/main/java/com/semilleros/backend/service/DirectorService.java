package com.semilleros.backend.service;

import com.semilleros.backend.dto.DirectorRequestDTO;
import com.semilleros.backend.dto.DirectorResponseDTO;
import com.semilleros.backend.entity.Director;
import com.semilleros.backend.exception.BusinessException;
import com.semilleros.backend.exception.ResourceNotFoundException;
import com.semilleros.backend.repository.DirectorRepository;
import com.semilleros.backend.repository.SemilleroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectorService {

    private final DirectorRepository directorRepository;
    private final SemilleroRepository semilleroRepository;

    @Transactional(readOnly = true)
    public List<DirectorResponseDTO> findAll() {
        return directorRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public DirectorResponseDTO findById(Long id) {
        Director director = findEntityById(id);
        return toResponseDTO(director);
    }

    @Transactional
    public DirectorResponseDTO create(DirectorRequestDTO request) {
        String correo = request.getCorreo().trim();
        if (directorRepository.existsByCorreoIgnoreCase(correo)) {
            throw new BusinessException("Ya existe un director registrado con el correo: " + correo);
        }

        Director director = Director.builder()
                .nombre(request.getNombre().trim())
                .correo(correo)
                .build();

        return toResponseDTO(directorRepository.save(director));
    }

    @Transactional
    public DirectorResponseDTO update(Long id, DirectorRequestDTO request) {
        Director director = findEntityById(id);
        String correo = request.getCorreo().trim();

        if (directorRepository.existsByCorreoIgnoreCaseAndIdNot(correo, id)) {
            throw new BusinessException("Ya existe otro director registrado con el correo: " + correo);
        }

        director.setNombre(request.getNombre().trim());
        director.setCorreo(correo);

        return toResponseDTO(directorRepository.save(director));
    }

    @Transactional
    public void delete(Long id) {
        Director director = findEntityById(id);
        if (semilleroRepository.existsByDirectorId(id)) {
            throw new BusinessException("No se puede eliminar el director porque tiene semilleros asociados");
        }
        directorRepository.delete(director);
    }

    public Director findEntityById(Long id) {
        return directorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Director no encontrado con id: " + id));
    }

    public DirectorResponseDTO toResponseDTO(Director director) {
        return DirectorResponseDTO.builder()
                .id(director.getId())
                .nombre(director.getNombre())
                .correo(director.getCorreo())
                .build();
    }
}

