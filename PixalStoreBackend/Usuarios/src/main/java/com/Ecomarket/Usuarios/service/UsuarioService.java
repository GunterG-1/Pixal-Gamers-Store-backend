package com.Ecomarket.Usuarios.service;

import com.Ecomarket.Usuarios.model.Rol;
import com.Ecomarket.Usuarios.model.Usuario;
import com.Ecomarket.Usuarios.repository.RolRepository;
import com.Ecomarket.Usuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    // Listar todos los usuarios
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    // Listar usuarios activos
    public List<Usuario> listarUsuariosActivos() {
        return usuarioRepository.findByActivoTrue();
    }

    // Listar usuarios desactivados
    public List<Usuario> listarUsuariosDesactivados() {
        return usuarioRepository.findByActivoFalse();
    }

    // Buscar usuario por ID
    public Optional<Usuario> buscarPorId(Long idUsuario) {
        return usuarioRepository.findById(idUsuario);
    }

    // Registrar un nuevo usuario (asigna rol automáticamente según el correo)
    public Usuario registrarUsuario(Usuario usuario) {
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new RuntimeException("El correo ya está en uso");
        }
        
        usuario.setDirUsuario(null);
        usuario.setMetodoPago(null);
        
        // Asignar rol automáticamente según el dominio del correo
        String nombreRol = usuario.getCorreo().toLowerCase().endsWith("@duocuc.cl") ? "ADMIN" : "CLIENTE";
        Rol rol = rolRepository.findByNombreRol(nombreRol);
        
        if (rol != null) {
            usuario.getRoles().add(rol);
        }
        
        usuario.setActivo(true);
        return usuarioRepository.save(usuario);
    }

    // Crear usuario con rol específico (validación para ADMIN)
    public Usuario crearUsuarioConRol(Usuario usuario, String nombreRol) {
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new RuntimeException("El correo ya está en uso");
        }
        
        // Validar que solo correos @duocuc.cl puedan ser ADMIN
        if ("ADMIN".equalsIgnoreCase(nombreRol) && !usuario.getCorreo().toLowerCase().endsWith("@duocuc.cl")) {
            throw new RuntimeException("Solo los correos con dominio @duocuc.cl pueden tener el rol de ADMIN");
        }
        
        Rol rol = rolRepository.findByNombreRol(nombreRol);
        if (rol == null) {
            throw new RuntimeException("Rol no encontrado");
        }
        usuario.getRoles().add(rol);
        usuario.setActivo(true);
        return usuarioRepository.save(usuario);
    }

    // Actualizar usuario existente
    public Usuario actualizarUsuario(Long idUsuario, Usuario usuarioActualizado) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setNombreUsuario(usuarioActualizado.getNombreUsuario());
        usuario.setApellidoUsuario(usuarioActualizado.getApellidoUsuario());
        usuario.setCorreo(usuarioActualizado.getCorreo());
        usuario.setContrasena(usuarioActualizado.getContrasena());
        usuario.setDirUsuario(usuarioActualizado.getDirUsuario());
        usuario.setMetodoPago(usuarioActualizado.getMetodoPago());
        usuario.setTelefono(usuarioActualizado.getTelefono());
        return usuarioRepository.save(usuario);
    }

    // Activar o desactivar usuario
    public Usuario cambiarEstadoUsuario(Long idUsuario, boolean activo) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setActivo(activo);
        return usuarioRepository.save(usuario);
    }

    // Eliminar usuario
    public void eliminarUsuario(Long idUsuario) {
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(idUsuario);
    }

    // Login (autenticación simple)
    public Usuario login(String correo, String contrasena) {
        return usuarioRepository.findByCorreoAndContrasena(correo, contrasena)
                .orElse(null);
    }

    // Verificar si un usuario es admin por su correo
    public boolean esAdmin(String correo) {
        return correo != null && correo.toLowerCase().endsWith("@duocuc.cl");
    }
}
