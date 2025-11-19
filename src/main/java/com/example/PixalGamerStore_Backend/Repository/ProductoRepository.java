package com.example.PixalGamerStore_Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.PixalGamerStore_Backend.Model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

}
