package com.PixalStoreBackend.Venta.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "carrito_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "carrito")
public class CarritoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idItem;

    @ManyToOne
    @JoinColumn(name = "idCarrito", nullable = false)
    @JsonBackReference
    private Carrito carrito;

    @Column(nullable = false)
    private Long idProducto;

    @Column(length = 100, nullable = false)
    private String nombreProducto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Double precioUnitario;

    public Double getSubtotal() {
        if (precioUnitario != null && cantidad != null) {
            return precioUnitario * cantidad;
        }
        return 0.0;
    }
}
