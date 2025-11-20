package com.PixalStoreBackend.Venta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PixalStoreBackend.Venta.model.CarritoItem;

@Repository
public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {
}
