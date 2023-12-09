package com.ravsdev.ropa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PromocionDTO {
    private String id;
    private BigDecimal descuento;
    private String nombre;
}
