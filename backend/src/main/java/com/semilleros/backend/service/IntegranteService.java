package com.semilleros.backend.service;

import com.semilleros.backend.dto.IntegranteRequestDTO;
import com.semilleros.backend.dto.IntegranteResponseDTO;
import com.semilleros.backend.entity.Integrante;
import com.semilleros.backend.entity.Semillero;
import com.semilleros.backend.exception.BusinessException;
import com.semilleros.backend.exception.ResourceNotFoundException;
import com.semilleros.backend.repository.IntegranteRepository;
import com.semilleros.backend.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IntegranteService {

    private final IntegranteRepository integranteRepository;
    private final ProductoRepository productoRepository;
    private final SemilleroService semilleroService;

    @Transactional(readOnly = true)
    public List<IntegranteResponseDTO> findAll() {
        return integranteRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<IntegranteResponseDTO> findBySemilleroId(Long semilleroId) {
        semilleroService.findEntityById(semilleroId);
        return integranteRepository.findBySemilleroId(semilleroId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public IntegranteResponseDTO findById(Long id) {
        return toResponseDTO(findEntityById(id));
    }

    @Transactional
    public IntegranteResponseDTO create(IntegranteRequestDTO request) {
        Semillero semillero = semilleroService.findEntityById(request.getSemilleroId());
        String codigo = request.getCodigo().trim();

        if (integranteRepository.existsBySemilleroIdAndCodigoIgnoreCase(semillero.getId(), codigo)) {
            throw new BusinessException("Ya existe un integrante con el código " + codigo + " en este semillero");
        }

        Integrante integrante = Integrante.builder()
                .semillero(semillero)
                .codigo(codigo)
                .nombre(request.getNombre().trim())
                .programa(request.getPrograma().trim())
                .esLider(request.getEsLider())
                .build();

        return toResponseDTO(integranteRepository.save(integrante));
    }

    @Transactional
    public IntegranteResponseDTO update(Long id, IntegranteRequestDTO request) {
        Integrante integrante = findEntityById(id);
        Semillero semillero = semilleroService.findEntityById(request.getSemilleroId());
        String codigo = request.getCodigo().trim();

        if (integranteRepository.existsBySemilleroIdAndCodigoIgnoreCaseAndIdNot(semillero.getId(), codigo, id)) {
            throw new BusinessException("Ya existe otro integrante con el código " + codigo + " en este semillero");
        }

        integrante.setSemillero(semillero);
        integrante.setCodigo(codigo);
        integrante.setNombre(request.getNombre().trim());
        integrante.setPrograma(request.getPrograma().trim());
        integrante.setEsLider(request.getEsLider());

        return toResponseDTO(integranteRepository.save(integrante));
    }

    @Transactional
    public void delete(Long id) {
        Integrante integrante = findEntityById(id);
        if (productoRepository.existsByIntegrantesId(id)) {
            throw new BusinessException("No se puede eliminar el integrante porque está asociado a uno o más productos");
        }
        integranteRepository.delete(integrante);
    }

    public Integrante findEntityById(Long id) {
        return integranteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Integrante no encontrado con id: " + id));
    }

    public IntegranteResponseDTO toResponseDTO(Integrante integrante) {
        Semillero semillero = integrante.getSemillero();
        return IntegranteResponseDTO.builder()
                .id(integrante.getId())
                .semilleroId(semillero.getId())
                .semilleroNombre(semillero.getNombre())
                .semilleroSigla(semillero.getSigla())
                .codigo(integrante.getCodigo())
                .nombre(integrante.getNombre())
                .programa(integrante.getPrograma())
                .esLider(integrante.getEsLider())
                .build();
    }
}

