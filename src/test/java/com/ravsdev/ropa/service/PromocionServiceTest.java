package com.ravsdev.ropa.service;

import com.ravsdev.ropa.entity.Promocion;
import com.ravsdev.ropa.repository.PromocionRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.ravsdev.ropa.service.Impl.PromocionServiceImpl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PromocionServiceTest {
    @Mock
    private PromocionRepository promocionRepository;

    @InjectMocks
    private PromocionServiceImpl promocionService;

    private Promocion promocion;
    private Promocion promocion2;

    private List<Promocion> promociones = new ArrayList<>();

    @BeforeEach
    public void init() {
        promocion = new Promocion();
        promocion.setNombre("Black Friday 2023");
        promocion.setId("BF2023");
        promocion.setDescuento(new BigDecimal("50.00"));

        promocion2 = new Promocion();
        promocion2.setNombre("Navidades 2023");
        promocion2.setId("N2023");
        promocion2.setDescuento(new BigDecimal("25.00"));

        promociones.add(promocion);
        promociones.add(promocion2);
    }

    @Test
    public void createPromocionTest() {
        Promocion esperado = promocion;
        when(promocionRepository.save(esperado)).thenReturn(esperado);

        Promocion resultado = promocionService.save(esperado);

        assertEquals(esperado.getId(), resultado.getId());
        assertEquals(esperado.getDescuento(), resultado.getDescuento());
        verify(promocionRepository).save(esperado);
    }

    @Test
    @DisplayName("Buscar promociones por nombre")
    public void obtenerPromocionesPorNombreTest() {
        final String nombre = "2023";
        Pageable pageable = PageRequest.of(0, 1);

        Page<Promocion> esperado = new PageImpl<>(promociones);

        when(promocionRepository.findByNombreContainingIgnoreCase(nombre, pageable)).thenReturn(Optional.of(esperado));

        final Page<Promocion> resultado = promocionService.findByNombre(nombre, pageable);

        assertEquals(esperado, resultado);

        verify(promocionRepository).findByNombreContainingIgnoreCase(nombre, pageable);

    }

}