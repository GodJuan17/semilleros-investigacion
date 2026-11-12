package com.semilleros.backend.repository;

import com.semilleros.backend.entity.Integrante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IntegranteRepository extends JpaRepository<Integrante, Long> {

    List<Integrante> findBySemilleroId(Long semilleroId);

    boolean existsBySemilleroId(Long semilleroId);

    boolean existsBySemilleroIdAndCodigoIgnoreCase(Long semilleroId, String codigo);

    boolean existsBySemilleroIdAndCodigoIgnoreCaseAndIdNot(Long semilleroId, String codigo, Long id);
}

