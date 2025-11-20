package com.PixalStoreBackend.Venta.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.PixalStoreBackend.Venta.model.Carrito;
import com.PixalStoreBackend.Venta.model.CarritoItem;
import com.PixalStoreBackend.Venta.repository.CarritoItemRepository;
import com.PixalStoreBackend.Venta.repository.CarritoRepository;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private CarritoItemRepository carritoItemRepository;

    // Obtener o crear carrito para un usuario
    public Carrito obtenerOCrearCarrito(Long idUsuario) {
        return carritoRepository.findByIdUsuario(idUsuario)
                .orElseGet(() -> {
                    Carrito nuevoCarrito = new Carrito();
                    nuevoCarrito.setIdUsuario(idUsuario);
                    return carritoRepository.save(nuevoCarrito);
                });
    }

    // Agregar item al carrito
    @Transactional
    public CarritoItem agregarItem(Long idUsuario, CarritoItem item) {
        Carrito carrito = obtenerOCrearCarrito(idUsuario);
        
        // Verificar si el producto ya existe en el carrito
        Optional<CarritoItem> itemExistente = carrito.getItems().stream()
                .filter(i -> i.getIdProducto().equals(item.getIdProducto()))
                .findFirst();
        
        if (itemExistente.isPresent()) {
            // Si existe, actualizar cantidad
            CarritoItem existing = itemExistente.get();
            existing.setCantidad(existing.getCantidad() + item.getCantidad());
            return carritoItemRepository.save(existing);
        } else {
            // Si no existe, agregar nuevo item
            item.setCarrito(carrito);
            return carritoItemRepository.save(item);
        }
    }

    // Actualizar cantidad de un item
    @Transactional
    public CarritoItem actualizarItem(Long idItem, Integer nuevaCantidad) {
        CarritoItem item = carritoItemRepository.findById(idItem)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));
        
        if (nuevaCantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a 0");
        }
        
        item.setCantidad(nuevaCantidad);
        return carritoItemRepository.save(item);
    }

    // Eliminar un item del carrito
    @Transactional
    public void eliminarItem(Long idItem) {
        if (!carritoItemRepository.existsById(idItem)) {
            throw new RuntimeException("Item no encontrado");
        }
        carritoItemRepository.deleteById(idItem);
    }

    // Vaciar carrito completo
    @Transactional
    public void vaciarCarrito(Long idUsuario) {
        Optional<Carrito> carritoOpt = carritoRepository.findByIdUsuario(idUsuario);
        if (carritoOpt.isPresent()) {
            Carrito carrito = carritoOpt.get();
            carrito.getItems().clear();
            carritoRepository.save(carrito);
        }
    }
}
