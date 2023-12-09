package com.ravsdev.ropa.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.NumberFormat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Prenda {
    @Id
    @Pattern(regexp = "^[SML]\\w{9}$", message = "Referencia alfanumérica de 10 caracteres, el primer carácter es la talla (S/M/L)")
    private String id;

    @NotNull(message = "Precio no proporcionado")
    @PositiveOrZero(message = "El precio debe ser mayor o igual que 0.")
    @NumberFormat(pattern = "#.##")
    private BigDecimal precio;

    @ElementCollection(targetClass = Categorias.class)
    @CollectionTable(name = "prenda_categoria", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "categoria")
    @Enumerated(EnumType.STRING)
    private Set<Categorias> categorias = new HashSet<>();

    @ManyToMany(cascade = { CascadeType.PERSIST,
            CascadeType.MERGE })
    @JoinTable(name = "prenda_promo", joinColumns = @JoinColumn(name = "prenda_id"), inverseJoinColumns = @JoinColumn(name = "promocion_id"))
    private Set<Promocion> promociones = new HashSet<>();

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date modifiedAt;

}
