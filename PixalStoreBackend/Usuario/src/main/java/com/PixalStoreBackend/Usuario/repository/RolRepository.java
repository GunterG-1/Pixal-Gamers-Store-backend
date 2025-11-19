package com.PixalStoreBackend.Usuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PixalStoreBackend.Usuario.model.Rol;

@Repository
public interface RolRepository extends JpaRepository<Rol,Long>{
	Rol findByNombreRol(String nombreRol);
}