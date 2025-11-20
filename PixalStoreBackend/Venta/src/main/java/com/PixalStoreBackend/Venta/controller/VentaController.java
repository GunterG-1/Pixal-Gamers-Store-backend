package com.PixalStoreBackend.Venta.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.PixalStoreBackend.Venta.model.Venta;
import com.PixalStoreBackend.Venta.repository.VentaRepository;
import com.PixalStoreBackend.Venta.service.VentaService;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;
    @Autowired
    private VentaRepository ventaRepository;

    @GetMapping
    public ResponseEntity<List<Venta>> listar(){
        List<Venta> ventas = ventaService.listarVentas();
        if(ventas.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ventas);
    }


    @PostMapping
    public ResponseEntity<Venta> registrarVenta(@RequestBody Venta venta) {
        try {
            Venta nuevaVenta = ventaService.registrarVenta(venta);
            return ResponseEntity.ok(nuevaVenta);
        } catch (Exception e) {
            e.printStackTrace(); 
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{idVenta}")
    public ResponseEntity<Venta> obtenerVenta(@PathVariable Long idVenta) {
        return ventaService.findById(idVenta)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{idVenta}")
    public ResponseEntity<Venta> actualizarVenta(@PathVariable Long idVenta, @RequestBody Venta ventaActualizada) {
        try {
            Venta actualizado = ventaService.actualizarVenta(idVenta, ventaActualizada);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
    }
}

    @DeleteMapping("/{idVenta}")
    public ResponseEntity<Void> eliminarVenta(@PathVariable Long idVenta) {
        if (ventaService.findById(idVenta).isPresent()) {
            ventaService.delete(idVenta);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/usuario/{idUsuario}")
    public List<Venta> obtenerVentasPorUsuario(@PathVariable Long idUsuario) {
        return ventaRepository.findByIdUsuario(idUsuario);
    }
    
    // Obtener historial de ventas del usuario
    @GetMapping("/historial")
    public ResponseEntity<List<Venta>> obtenerHistorial(@RequestParam Long idUsuario) {
        List<Venta> historial = ventaRepository.findByIdUsuario(idUsuario);
        return ResponseEntity.ok(historial);
    }
    
    // Actualizar estado de una venta
    @PutMapping("/{idVenta}/estado")
    public ResponseEntity<Venta> actualizarEstado(
            @PathVariable Long idVenta,
            @RequestBody Map<String, String> body) {
        try {
            String nuevoEstado = body.get("estado");
            Venta venta = ventaService.findById(idVenta)
                    .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
            
            venta.setEstado(nuevoEstado);
            Venta ventaActualizada = ventaRepository.save(venta);
            return ResponseEntity.ok(ventaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Checkout: crear venta a partir del carrito persistido
    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestParam Long idUsuario) {
        try {
            Venta venta = ventaService.checkoutDesdeCarrito(idUsuario);
            return ResponseEntity.ok(venta);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "checkout",
                "mensaje", e.getMessage()
            ));
        }
    }
}
