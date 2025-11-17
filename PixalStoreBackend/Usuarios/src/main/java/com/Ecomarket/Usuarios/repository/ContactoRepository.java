package com.Ecomarket.Usuarios.repository;

import com.Ecomarket.Usuarios.model.Contactos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactoRepository extends JpaRepository<Contactos, Long> {
}
