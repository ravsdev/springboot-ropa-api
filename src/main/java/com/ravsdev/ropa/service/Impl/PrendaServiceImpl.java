package com.ravsdev.ropa.service.Impl;

import java.util.Optional;

import com.ravsdev.ropa.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ravsdev.ropa.entity.Prenda;
import com.ravsdev.ropa.repository.PrendaRepository;
import com.ravsdev.ropa.service.PrendaService;

@Service
public class PrendaServiceImpl implements PrendaService {
    @Autowired
    private PrendaRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Page<Prenda> findAll(Pageable page) {
        return repository.findAll(page);
    }

    @Override
    public Page<Prenda> findByPromo(String promocion, Pageable page) {
        return repository.findByPromocion(promocion,page);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Prenda> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Prenda save(Prenda prenda) {
        return repository.save(prenda);
    }

    @Override
    public Prenda update(Prenda prenda) {
        Prenda updatePrenda = repository.findById(prenda.getId())
                .orElseThrow(() -> new NotFoundException("Prenda " + prenda.getId() + " no encontrada"));
        updatePrenda.setPrecio(prenda.getPrecio());
        updatePrenda.setCategorias(prenda.getCategorias());

        return repository.save(updatePrenda);

    }

    @Override
    public void delete(String id) {
        Prenda prenda = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Prenda " + id + " no encontrada"));
        repository.delete(prenda);
    }

}
