package com.ravsdev.ropa.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ravsdev.ropa.entity.Prenda;
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
public class PromocionPrendasDTO {
    private String id;
    private BigDecimal descuento;
    private String nombre;
    private Set<Prenda> prendas = new HashSet<>();

    public Set<String> getPrendas() {
        Set<String> listaPrendas = new HashSet<>();
        if (!this.prendas.isEmpty()) {
            listaPrendas = this.prendas.stream().map(prenda -> prenda.getId()).collect(Collectors.toSet());
        }
        return listaPrendas;
    }

}
