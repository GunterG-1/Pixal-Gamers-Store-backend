package com.PixalStoreBackend.Venta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.PixalStoreBackend.Venta.model.DetalleVenta;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {

}