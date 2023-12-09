package com.ravsdev.ropa.dto;

import com.ravsdev.ropa.entity.Categorias;
import com.ravsdev.ropa.entity.Promocion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PrendaPrecioPromocionDTO {
    private String id;
    private BigDecimal precio;
    private Set<Categorias> categorias;
    private Set<Promocion> promociones;
    private BigDecimal precioPromocion;

    public BigDecimal getPrecioPromocion() {
        if (promociones != null) {
            BigDecimal precioPromo = this.precio;
            for (Promocion promo : promociones) {
                precioPromo = precioPromo
                        .subtract(precioPromo
                                .multiply(promo.getDescuento()
                                        .divide(BigDecimal.valueOf(100))));
            }
            return precioPromo;
        }
        return this.precio;
    }

    public Set<String> getPromociones() {
        Set<String> promos = new HashSet<>();
        if (!this.promociones.isEmpty()) {
            promos = this.promociones.stream().map(promo -> promo.getId()).collect(Collectors.toSet());
        }
        return promos;
    }
}