package com.ravsdev.ropa.repository;

import com.ravsdev.ropa.entity.Promocion;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromocionRepository extends JpaRepository<Promocion, String> {
    @Query(value = "SELECT * FROM PROMOCION WHERE LOWER(nombre) LIKE %?%", nativeQuery = true)
    public Optional<List<Promocion>> findByNombre(String nombre);

    public Optional<Page<Promocion>> findByNombreContainingIgnoreCase(String nombre, Pageable page);
}
