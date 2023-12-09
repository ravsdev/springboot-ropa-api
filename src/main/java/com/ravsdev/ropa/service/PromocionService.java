package com.ravsdev.ropa.service;

import com.ravsdev.ropa.entity.Promocion;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PromocionService {
    public Page<Promocion> findAll(Pageable page);

    public Optional<Promocion> findById(String id);

    public Page<Promocion> findByNombre(String nombre, Pageable page);

    public Promocion save(Promocion promocion);

    public Promocion update(Promocion promocion);

    public void delete(String id);

    public void addPrendasToPromocion(String promocion_id, List<String> prenda_ids);

    public void deletePrendasFromPromocion(String promocion_id, List<String> prenda_ids);

}
