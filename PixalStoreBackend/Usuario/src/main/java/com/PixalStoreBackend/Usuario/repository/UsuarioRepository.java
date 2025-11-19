package com.PixalStoreBackend.Usuario.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PixalStoreBackend.Usuario.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
       
        Optional<Usuario> findByCorreo(String correo);
    boolean existsByCorreo(String correo);

    // Agrega este método para login:
    Optional<Usuario> findByCorreoAndContrasena(String correo, String contrasena);
    List<Usuario> findByActivoTrue();
    List<Usuario> findByActivoFalse();
}
