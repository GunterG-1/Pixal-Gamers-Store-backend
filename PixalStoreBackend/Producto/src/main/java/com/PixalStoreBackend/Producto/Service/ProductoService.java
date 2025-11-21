package com.PixalStoreBackend.Producto.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.PixalStoreBackend.Producto.Model.Producto;
import com.PixalStoreBackend.Producto.Repository.ProductoRepository;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }
    public Producto getProductoById(Long id) {
        return productoRepository.findById(id).orElse(null);
    }
    public Producto saveProducto(Producto producto) {
        // Si no viene stock (null), asignar por defecto un valor aleatorio entre 1 y 50
        if (producto.getStock() == null) {
            int randomStock = 1 + (int) (Math.random() * 50);
            producto.setStock(randomStock);
        }
        return productoRepository.save(producto);
    }
    public void deleteProducto(Long id) {
        productoRepository.deleteById(id);
    }
    
    public List<Producto> getProductosDestacados() {
        return productoRepository.findByDestacadoTrue();
    }

    // Decrementa stock al vender cantidadVendida
    @Transactional
    public Producto actualizarStock(Long idProducto, int cantidadVendida) {
        if (cantidadVendida <= 0) {
            throw new IllegalArgumentException("cantidadVendida debe ser > 0");
        }
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + idProducto));
        Integer stockActual = producto.getStock() == null ? 0 : producto.getStock();
        if (stockActual < cantidadVendida) {
            throw new RuntimeException("Stock insuficiente para producto " + idProducto + ": disponible=" + stockActual + ", requerido=" + cantidadVendida);
        }
        producto.setStock(stockActual - cantidadVendida);
        return productoRepository.save(producto);
    }
}
