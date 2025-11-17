package com.Ecomarket.Usuarios.controller;

import com.Ecomarket.Usuarios.model.Usuario;
import com.Ecomarket.Usuarios.service.UsuarioService;
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
    @PostMapping("/registrar")
    public ResponseEntity<Usuario> registrar(@RequestBody Usuario usuario) {
        try {
            Usuario nuevo = usuarioService.registrarUsuario(usuario);
            return ResponseEntity.ok(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String correo = body.get("correo");
        String contrasena = body.get("contrasena");
        String confirmarContrasena = body.get("confirmarContrasena");

        // Verificación: pedir la contraseña nuevamente y validar que coincidan
        if (confirmarContrasena == null || !contrasena.equals(confirmarContrasena)) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "validacion",
                "mensaje", "Las contraseñas no coinciden. Por favor, ingrésala nuevamente"
            ));
        }

        Usuario usuario = usuarioService.login(correo, contrasena);
        if (usuario == null) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
        return ResponseEntity.ok(usuario);
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
