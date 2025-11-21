package com.PixalStoreBackend.Usuario.model;


import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAlias;
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
    @JsonAlias({"nombre"})
    private String nombreUsuario;

    @Column(unique = false, length = 25, nullable = false)
    @JsonAlias({"apellido"})
    private String apellidoUsuario; 

    @Column(unique = true, length = 50, nullable = false)
    @JsonAlias({"email"})
    private String correo;

    @Column(unique = false, length = 25, nullable = false)
    @JsonAlias({"password"})
    private String contrasena;

   
    @Column(length = 200, nullable = true)
    @JsonAlias({"direccion","dir"})
    private String dirUsuario;
 
    @Column(length = 100, nullable = true)
    private String metodoPago;

    @Column(length = 15, nullable = true)
    @JsonAlias({"phone"})
    private String telefono;

    private boolean activo = true;

    @ManyToMany(mappedBy = "usuarios", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Rol> roles = new HashSet<>();

}