package com.ravsdev.ropa.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ravsdev.ropa.entity.Prenda;

public interface PrendaService {
    public Page<Prenda> findAll(Pageable page);
    public Page<Prenda> findByPromo(String promocion, Pageable page);
    public Optional<Prenda> findById(String id);

    public Prenda save(Prenda prenda);

    public Prenda update(Prenda prenda);

    public void delete(String id);
}