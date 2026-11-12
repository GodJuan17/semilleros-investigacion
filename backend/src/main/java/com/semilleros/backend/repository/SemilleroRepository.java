package com.semilleros.backend.repository;

import com.semilleros.backend.entity.Semillero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SemilleroRepository extends JpaRepository<Semillero, Long> {

    boolean existsBySiglaIgnoreCase(String sigla);

    boolean existsBySiglaIgnoreCaseAndIdNot(String sigla, Long id);

    boolean existsByDirectorId(Long directorId);

    boolean existsByLineasId(Long lineaId);
}

