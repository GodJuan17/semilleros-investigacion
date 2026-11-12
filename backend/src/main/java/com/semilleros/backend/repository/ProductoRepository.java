package com.semilleros.backend.repository;

import com.semilleros.backend.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findBySemilleroId(Long semilleroId);

    boolean existsBySemilleroId(Long semilleroId);

    boolean existsByIntegrantesId(Long integranteId);

    boolean existsBySemilleroIdAndNombreIgnoreCase(Long semilleroId, String nombre);

    boolean existsBySemilleroIdAndNombreIgnoreCaseAndIdNot(Long semilleroId, String nombre, Long id);
}

