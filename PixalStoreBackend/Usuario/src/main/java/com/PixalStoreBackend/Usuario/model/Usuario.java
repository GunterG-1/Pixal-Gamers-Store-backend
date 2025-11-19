package com.PixalStoreBackend.Usuario.model;


import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(unique = false, length = 25, nullable = false)
    private String nombreUsuario;

    @Column(unique = false, length = 25, nullable = false)
    private String apellidoUsuario; 

    @Column(unique = true, length = 50, nullable = false)
    private String correo;

    @Column(unique = false, length = 25, nullable = false)
    private String contrasena;

   
    @Column(length = 200, nullable = true)
    private String dirUsuario;
 
    @Column(length = 100, nullable = true)
    private String metodoPago;

    @Column(length = 9, nullable = false)
    private int telefono;

    private boolean activo = true;

    @ManyToMany(mappedBy = "usuarios")
    private Set<Rol> roles = new HashSet<>();

}