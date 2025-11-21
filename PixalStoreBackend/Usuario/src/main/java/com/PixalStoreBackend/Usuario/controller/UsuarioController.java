package com.PixalStoreBackend.Usuario.controller;

import com.PixalStoreBackend.Usuario.model.Usuario;
import com.PixalStoreBackend.Usuario.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Listar todos los usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    // Listar usuarios activos
    @GetMapping("/activos")
    public ResponseEntity<List<Usuario>> listarActivos() {
        return ResponseEntity.ok(usuarioService.listarUsuariosActivos());
    }

    // Listar usuarios desactivados
    @GetMapping("/desactivados")
    public ResponseEntity<List<Usuario>> listarDesactivados() {
        return ResponseEntity.ok(usuarioService.listarUsuariosDesactivados());
    }

    // Ver perfil por ID
    @GetMapping("/{idUsuario}")
    public ResponseEntity<Usuario> verPerfil(@PathVariable Long idUsuario) {
        return usuarioService.buscarPorId(idUsuario)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Registrar usuario (asigna rol automáticamente según el correo)
    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        try {
            Usuario nuevo = usuarioService.registrarUsuario(usuario);
            return ResponseEntity.ok(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "registro",
                "mensaje", e.getMessage()
            ));
        }
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String correo = body.getOrDefault("correo", body.get("email"));
        String contrasena = body.getOrDefault("contrasena", body.get("password"));

        if (correo == null || contrasena == null) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "validacion",
                "mensaje", "Correo y contraseña son requeridos"
            ));
        }

        if (correo.isBlank() || contrasena.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "validacion",
                "mensaje", "Correo y contraseña no pueden estar vacíos"
            ));
        }

        Usuario usuario = usuarioService.login(correo, contrasena);
        
        if (usuario == null) {
            return ResponseEntity.status(401).body(Map.of(
                "error", "autenticacion",
                "mensaje", "Credenciales inválidas"
            ));
        }
        
        if (!usuario.isActivo()) {
            return ResponseEntity.status(403).body(Map.of(
                "error", "cuenta_desactivada",
                "mensaje", "Tu cuenta está desactivada"
            ));
        }
        
        return ResponseEntity.ok(usuario);
    }

    // Logout (Simple - podrías implementar invalidación de token JWT aquí)
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // Por ahora, solo confirmamos que el logout fue exitoso
        // En una implementación real, aquí invalidarías el token JWT
        return ResponseEntity.ok(Map.of("mensaje", "Sesión cerrada exitosamente"));
    }


    @GetMapping("/validate")
    public ResponseEntity<?> validateSession() {
        
        return ResponseEntity.ok(Map.of("valido", true));
    }

    // Obtener perfil del usuario autenticado (Por ahora usa ID en query param)
    @GetMapping("/perfil")
    public ResponseEntity<?> getPerfil(@RequestParam Long idUsuario) {
        return usuarioService.buscarPorId(idUsuario)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Actualizar perfil del usuario autenticado (solo nombre, teléfono, dirección)
    @PutMapping("/{idUsuario}/perfil")
    public ResponseEntity<?> updatePerfil(@PathVariable Long idUsuario, @RequestBody Map<String, String> datos) {
        try {
            Usuario actualizado = usuarioService.actualizarPerfil(idUsuario, datos);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "actualizacion",
                "mensaje", e.getMessage()
            ));
        }
    }

    // Actualizar usuario
    @PutMapping("/{idUsuario}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long idUsuario, @RequestBody Usuario usuarioActualizado) {
        try {
            Usuario actualizado = usuarioService.actualizarUsuario(idUsuario, usuarioActualizado);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Cambiar estado del usuario (activar/desactivar)
    @PatchMapping("/{idUsuario}/estado")
    public ResponseEntity<Usuario> cambiarEstado(@PathVariable Long idUsuario, @RequestParam boolean activo) {
        try {
            Usuario usuario = usuarioService.cambiarEstadoUsuario(idUsuario, activo);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar usuario
    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Void> eliminar(@PathVariable Long idUsuario) {
        try {
            usuarioService.eliminarUsuario(idUsuario);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Verificar si un correo es de admin
    @GetMapping("/es-admin")
    public ResponseEntity<Map<String, Boolean>> esAdmin(@RequestParam String correo) {
        boolean esAdmin = usuarioService.esAdmin(correo);
        return ResponseEntity.ok(Map.of("esAdmin", esAdmin));
    }
}

