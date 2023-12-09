package com.ravsdev.ropa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ravsdev.ropa.entity.Prenda;

@Repository
public interface PrendaRepository extends JpaRepository<Prenda, String> {
    @Query(value = "SELECT * FROM PRENDA LEFT JOIN PRENDA_PROMO ON ID=PRENDA_ID WHERE PROMOCION_ID=?",
            nativeQuery = true)
    public Page<Prenda> findByPromocion(String promocion, Pageable page);

    @Query(value = "SELECT * FROM PRENDA LEFT JOIN PRENDA_PROMO ON ID WHERE CATEGORIA=?",
            nativeQuery = true)
    public Page<Prenda> findByCategoria(String categoria, Pageable page);
}
