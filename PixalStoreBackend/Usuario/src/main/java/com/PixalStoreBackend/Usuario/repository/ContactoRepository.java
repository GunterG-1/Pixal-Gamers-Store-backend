package com.PixalStoreBackend.Usuario.repository;

import com.PixalStoreBackend.Usuario.model.Contactos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactoRepository extends JpaRepository<Contactos, Long> {
}
