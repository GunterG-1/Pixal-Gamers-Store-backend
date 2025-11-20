package com.PixalStoreBackend.Venta.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.PixalStoreBackend.Venta.model.Carrito;
import com.PixalStoreBackend.Venta.model.CarritoItem;
import com.PixalStoreBackend.Venta.service.CarritoService;

@RestController
@RequestMapping("/api/ventas/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    // Obtener carrito del usuario
    @GetMapping
    public ResponseEntity<Carrito> obtenerCarrito(@RequestParam Long idUsuario) {
        Carrito carrito = carritoService.obtenerOCrearCarrito(idUsuario);
        return ResponseEntity.ok(carrito);
    }

    // Agregar item al carrito
    @PostMapping("/items")
    public ResponseEntity<CarritoItem> agregarItem(
            @RequestParam Long idUsuario, 
            @RequestBody CarritoItem item) {
        try {
            CarritoItem nuevoItem = carritoService.agregarItem(idUsuario, item);
            return ResponseEntity.ok(nuevoItem);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Actualizar cantidad de un item
    @PutMapping("/items/{idItem}")
    public ResponseEntity<CarritoItem> actualizarItem(
            @PathVariable Long idItem,
            @RequestBody Map<String, Integer> body) {
        try {
            Integer cantidad = body.get("cantidad");
            CarritoItem itemActualizado = carritoService.actualizarItem(idItem, cantidad);
            return ResponseEntity.ok(itemActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar un item del carrito
    @DeleteMapping("/items/{idItem}")
    public ResponseEntity<Void> eliminarItem(@PathVariable Long idItem) {
        try {
            carritoService.eliminarItem(idItem);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Vaciar carrito completo
    @DeleteMapping
    public ResponseEntity<Void> vaciarCarrito(@RequestParam Long idUsuario) {
        carritoService.vaciarCarrito(idUsuario);
        return ResponseEntity.noContent().build();
    }
}
