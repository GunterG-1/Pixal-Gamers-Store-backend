package com.PixalStoreBackend.Venta.service;




import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.PixalStoreBackend.Venta.client.ProductoClient;
import com.PixalStoreBackend.Venta.client.ProductoClient.ProductoDTO;
import com.PixalStoreBackend.Venta.client.UsuarioClient;
import com.PixalStoreBackend.Venta.client.UsuarioClient.UsuarioDTO;
import com.PixalStoreBackend.Venta.model.Carrito;
import com.PixalStoreBackend.Venta.model.CarritoItem;
import com.PixalStoreBackend.Venta.repository.CarritoRepository;
import com.PixalStoreBackend.Venta.repository.CarritoItemRepository;
import com.PixalStoreBackend.Venta.model.DetalleVenta;
import com.PixalStoreBackend.Venta.model.Venta;

import com.PixalStoreBackend.Venta.repository.VentaRepository;


import jakarta.transaction.Transactional;

@Service
@Transactional
public class VentaService {
    
    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductoClient productoClient;

    @Autowired
    private UsuarioClient usuarioClient;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private CarritoItemRepository carritoItemRepository;

    

   

    public List<Venta> listarVentas(){
        return ventaRepository.findAll();
    }

    public Venta registrarVenta(Venta venta) {
        // Obtener datos del usuario
        UsuarioDTO usuario = usuarioClient.obtenerPorId(venta.getIdUsuario());
        
        venta.setNombreUsuario(usuario.getNombreUsuario());
        venta.setApellidoUsuario(usuario.getApellidoUsuario());
        venta.setCorreo(usuario.getCorreo());
        venta.setDirUsuario(usuario.getDirUsuario());

        if (venta.getDetalle() == null || venta.getDetalle().isEmpty()) {
            throw new IllegalArgumentException("La venta no contiene detalles");
        }

        // Procesar los detalles de venta
        for (DetalleVenta detalle : venta.getDetalle()) {
            ProductoDTO producto = productoClient.obtenerProducto(detalle.getIdProducto());
            if (producto == null) {
                throw new IllegalArgumentException("Producto no encontrado con ID: " + detalle.getIdProducto());
            }
            detalle.setNombreProducto(producto.getNombreProducto());
            detalle.setPrecioUnitario(producto.getPrecioUnitario());
            detalle.setVenta(venta);
        }

        // Guardar venta con detalles (cascade se encarga de DetalleVenta)
        Venta ventaGuardada = ventaRepository.save(venta);

        // Actualizar stock del producto
        for (DetalleVenta d : venta.getDetalle()) {
            productoClient.actualizarStock(d.getIdProducto(), d.getCantidad());
        }

        return ventaGuardada;

    }

    
    public Optional<Venta> findById(Long idVenta){
        return ventaRepository.findById(idVenta);
    }
    public Venta actualizarVenta(Long idVenta, Venta ventaActualizada) {
        Venta venta = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new RuntimeException("venta no encontrada"));

        for (int i = 0; i < venta.getDetalle().size(); i++) {
            DetalleVenta detalleExistente = venta.getDetalle().get(i);
            DetalleVenta detalleNuevo = ventaActualizada.getDetalle().get(i);

            // 1. Revertir el stock anterior
            productoClient.actualizarStock(detalleExistente.getIdProducto(), -detalleExistente.getCantidad());

            // 2. Actualizar los datos del detalle
            detalleExistente.setIdProducto(detalleNuevo.getIdProducto());
            detalleExistente.setCantidad(detalleNuevo.getCantidad());

            // 3. Aplicar el nuevo descuento de stock
            productoClient.actualizarStock(detalleExistente.getIdProducto(), detalleExistente.getCantidad());
        }

        return ventaRepository.save(venta);
    }

    public Venta save(Venta venta) {
        return ventaRepository.save(venta);
    }

    public void delete(Long id) {
        ventaRepository.deleteById(id);
    }

    // Checkout: crear Venta desde carrito de usuario y vaciar carrito
    public Venta checkoutDesdeCarrito(Long idUsuario) {
        Carrito carrito = carritoRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException("Carrito vacío o no encontrado para usuario:" + idUsuario));
        if (carrito.getItems().isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        UsuarioDTO usuario = usuarioClient.obtenerPorId(idUsuario);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado para checkout");
        }
        
       
        if (usuario.getDirUsuario() == null || usuario.getDirUsuario().isBlank()) {
            throw new RuntimeException("Debes agregar una dirección de envío en tu perfil antes de realizar la compra");
        }

        Venta venta = new Venta();
        venta.setIdUsuario(idUsuario);
        venta.setNombreUsuario(usuario.getNombreUsuario());
        venta.setApellidoUsuario(usuario.getApellidoUsuario());
        venta.setCorreo(usuario.getCorreo());
        venta.setDirUsuario(usuario.getDirUsuario());
        venta.setEstado("PENDIENTE");
        // fechaVenta se establece en @PrePersist, pero podemos adelantar
        venta.setFechaVenta(new java.util.Date());

        // Convertir items a detalle
        List<DetalleVenta> detalles = carrito.getItems().stream().map(item -> {
            DetalleVenta d = new DetalleVenta();
            d.setIdProducto(item.getIdProducto());
            d.setCantidad(item.getCantidad());
            d.setNombreProducto(item.getNombreProducto());
            // precioUnitario en DetalleVenta es BigDecimal
            d.setPrecioUnitario(java.math.BigDecimal.valueOf(item.getPrecioUnitario()));
            d.setVenta(venta);
            return d;
        }).toList();
        venta.setDetalle(detalles);

        // Guardar venta con detalles
        Venta guardada = ventaRepository.save(venta);

        // Descontar stock por cada ítem vendido
        for (DetalleVenta d : guardada.getDetalle()) {
            productoClient.actualizarStock(d.getIdProducto(), d.getCantidad());
        }

        // Vaciar carrito (orphanRemoval se encarga al quitar items)
        carrito.getItems().clear();
        carritoRepository.save(carrito);

        return guardada;
    }

   
}

