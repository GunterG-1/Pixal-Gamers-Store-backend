package com.PixalStoreBackend.Producto.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import com.PixalStoreBackend.Producto.Model.Producto;
import com.PixalStoreBackend.Producto.Service.ProductoService;


@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<Producto> getAllProductos() {
        return productoService.getAllProductos();
    }
    @GetMapping("/{id}")
    public Producto getProductoById(@PathVariable Long id) {
        return productoService.getProductoById(id);
    }
    @PostMapping
    public Producto createProducto(@RequestBody Producto producto) {
        return productoService.saveProducto(producto);
    }
    @PutMapping("/{id}")
    public Producto updateProducto(@PathVariable Long id, @RequestBody Producto productoDetails) {
        Producto producto = productoService.getProductoById(id);
        if (producto != null) {
            producto.setNombre(productoDetails.getNombre());
            producto.setPrecio(productoDetails.getPrecio());
            producto.setDescripcion(productoDetails.getDescripcion());
            producto.setCategoria(productoDetails.getCategoria());
            producto.setCategorialabel(productoDetails.getCategorialabel());
            producto.setImagen(productoDetails.getImagen());
            producto.setDestacado(productoDetails.isDestacado());
            producto.setStock(productoDetails.getStock());
            return productoService.saveProducto(producto);
        }
        return null;
    }
    @DeleteMapping("/{id}")
    public void deleteProducto(@PathVariable Long id) {
        productoService.deleteProducto(id);
    }
    
    @GetMapping("/destacados")
    public List<Producto> getProductosDestacados() {
        return productoService.getProductosDestacados();
    }

    // Actualizar stock al vender (cantidadVendida se descuenta)
    @PutMapping("/{id}/actualizarStock")
    public ResponseEntity<?> actualizarStock(@PathVariable Long id, @RequestParam int cantidadVendida) {
        try {
            Producto producto = productoService.actualizarStock(id, cantidadVendida);
            return ResponseEntity.ok(producto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "validacion",
                "mensaje", e.getMessage()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(409).body(Map.of(
                "error", "stock_insuficiente",
                "mensaje", e.getMessage()
            ));
        }
    }
}