package com.ravsdev.ropa.dto;

import com.ravsdev.ropa.entity.Categorias;
import com.ravsdev.ropa.entity.Promocion;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PrendaDTO {
    private String id;
    private BigDecimal precio;
    private Set<Categorias> categorias = new HashSet<>();
    private Set<Promocion> promociones = new HashSet<>();

    public Set<String> getPromociones() {
        Set<String> promos = new HashSet<>();
        if (!this.promociones.isEmpty()) {
            promos = this.promociones.stream().map(promo -> promo.getId()).collect(Collectors.toSet());
        }
        return promos;
    }

}
