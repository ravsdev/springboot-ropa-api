package com.ravsdev.ropa.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.NumberFormat;

//import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Promocion {
    @Id
    private String id;

    @NumberFormat(pattern = "#.##")
    @DecimalMin(value = "0.00")
    @DecimalMax(value = "100.00")
    private BigDecimal descuento;

    @NotNull
    private String nombre;

    // @JsonIgnore
    @ManyToMany(mappedBy = "promociones")
    private Set<Prenda> prendas = new HashSet<>();

    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date modifiedAt;

    @PreRemove
    private void removePrendasAssociations() {
        for (Prenda prenda : this.prendas) {
            prenda.getPromociones().remove(this);
        }
    }
}
