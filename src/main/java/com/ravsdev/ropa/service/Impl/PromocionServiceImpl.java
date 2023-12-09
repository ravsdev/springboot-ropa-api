package com.ravsdev.ropa.service.Impl;

import com.ravsdev.ropa.entity.Prenda;
import com.ravsdev.ropa.entity.Promocion;
import com.ravsdev.ropa.exceptions.NotFoundException;
import com.ravsdev.ropa.repository.PrendaRepository;
import com.ravsdev.ropa.repository.PromocionRepository;
import com.ravsdev.ropa.service.PromocionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PromocionServiceImpl implements PromocionService {

    @Autowired
    PromocionRepository repository;
    @Autowired
    private PrendaRepository prendaRepository;

    @Override
    public Page<Promocion> findAll(Pageable page) {
        return repository.findAll(page);

    }

    @Override
    public Optional<Promocion> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Page<Promocion> findByNombre(String nombre, Pageable page) {
        return repository.findByNombreContainingIgnoreCase(nombre, page).orElseThrow();
    }

    @Override
    public Promocion save(Promocion promocion) {
        return repository.save(promocion);
    }

    @Override
    public Promocion update(Promocion promocion) {
        Promocion updatePromocion = repository.findById(promocion.getId())
                .orElseThrow(() -> new NotFoundException("Promoción " + promocion.getId() + " no encontrada"));
        updatePromocion.setDescuento(promocion.getDescuento());
        updatePromocion.setNombre(promocion.getNombre());

        return repository.save(updatePromocion);
    }

    @Override
    public void delete(String id) {
        Promocion promocion = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Promocion con ID: " + id + " no encontrada."));
        repository.delete(promocion);
    }

    @Override
    public void addPrendasToPromocion(String promocion_id, List<String> prenda_ids) {
        final Promocion promocion = repository.findById(promocion_id)
                .orElseThrow(() -> new NotFoundException("Promocion " + promocion_id + " no encontrada"));

        prenda_ids.forEach(prendaId -> {
            Prenda prenda = prendaRepository.findById(prendaId)
                    .orElseThrow(() -> new NotFoundException("Prenda " + prendaId + " no encontrada"));
            Set<Promocion> promociones;
            promociones = prenda.getPromociones();
            promociones.add(promocion);
            prenda.setPromociones(promociones);
            prendaRepository.save(prenda);
        });
    }

    @Override
    public void deletePrendasFromPromocion(String promocion_id, List<String> prenda_ids) {
        final Promocion promocion = repository.findById(promocion_id)
                .orElseThrow(() -> new NotFoundException("Promocion " + promocion_id + " no encontrada"));

        prenda_ids.forEach(prendaId -> {
            Prenda prenda = prendaRepository.findById(prendaId)
                    .orElseThrow(() -> new NotFoundException("Prenda " + prendaId + " no encontrada"));
            Set<Promocion> promociones;
            promociones = prenda.getPromociones();
            promociones.remove(promocion);
            prenda.setPromociones(promociones);
            prendaRepository.save(prenda);
        });
    }
}
