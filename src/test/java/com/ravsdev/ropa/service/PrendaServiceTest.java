package com.ravsdev.ropa.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.*;

import org.mockito.junit.jupiter.MockitoExtension;

import com.ravsdev.ropa.entity.Prenda;
import com.ravsdev.ropa.repository.PrendaRepository;
import com.ravsdev.ropa.service.Impl.PrendaServiceImpl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class PrendaServiceTest {
    @Mock
    private PrendaRepository prendaRepository;

    @InjectMocks
    private PrendaServiceImpl prendaService;

    private Prenda prenda;
    private List<Prenda> prendas = new ArrayList<>();

    @BeforeEach
    public void init() {

        prenda = new Prenda("M123456789", new BigDecimal("15.50"), Collections.emptySet(), Collections.emptySet(),
                new Date(), new Date());
        prendas.add(prenda);
    }

    @Test
    @DisplayName("Crear una prenda nueva")
    public void createPrendaTest() {
        Prenda esperado = prenda;
        when(prendaRepository.save(esperado)).thenReturn(esperado);

        Prenda resultado = prendaService.save(esperado);

        assertEquals(esperado.getId(), resultado.getId());
        assertEquals(esperado.getPrecio(), resultado.getPrecio());
        verify(prendaRepository).save(esperado);
    }

    @Test
    @DisplayName("Actualizar una prenda")
    public void updatePrendaTest() {
        Prenda esperado = prenda;

        when(prendaRepository.findById(anyString())).thenReturn(Optional.of(esperado));
        when(prendaRepository.save(esperado)).thenReturn(esperado);

        Prenda resultado = prendaService.update(esperado);

        assertEquals(esperado.getId(), resultado.getId());
        assertEquals(esperado.getPrecio(), resultado.getPrecio());
        verify(prendaRepository).save(esperado);
    }

    @Test
    @DisplayName("Obtener una prenda por ID")
    public void findByIdPrendaTest() {
        final String prendaId = "M123456789";
        Prenda esperado = prenda;

        when(prendaRepository.findById(prendaId)).thenReturn(Optional.of(esperado));

        final Optional<Prenda> resultado = prendaService.findById(prendaId);

        assertTrue(resultado.isPresent());
        assertEquals(esperado.getId(), resultado.get().getId());
        assertEquals(esperado.getPrecio(), resultado.get().getPrecio());

    }

    @Test
    @DisplayName("Resultado vacío con una ID de prenda inexistente")
    public void findByIdPrendaFailsTest() {
        final Optional<Prenda> resultado = prendaService.findById(anyString());
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Obtener una lista de prendas")
    public void findAllPrendas() {
        Pageable pageable = PageRequest.of(0, 1);

        Page<Prenda> esperado = new PageImpl<>(prendas);

        when(prendaRepository.findAll(pageable)).thenReturn(esperado);

        final Page<Prenda> resultado = prendaService.findAll(pageable);

        assertEquals(esperado, resultado);

    }
}
