package com.PixalStoreBackend.Producto.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PixalStoreBackend.Producto.Model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

}
