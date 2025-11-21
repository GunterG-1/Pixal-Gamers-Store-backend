package com.PixalStoreBackend.Venta.client;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ProductoClient {
    @Autowired
    private RestTemplate restTemplate;

    public ProductoDTO obtenerProducto(Long idProducto) {
        String url = "http://localhost:8082/api/productos/{idProducto}";
        Map<String, Long> params = new HashMap<>();
        params.put("idProducto", idProducto);
        return restTemplate.getForObject(url, ProductoDTO.class, params);
    }
     public void actualizarStock(Long idProducto, int cantidadVendida) {
        String url = "http://localhost:8082/api/productos/" + idProducto + "/actualizarStock?cantidadVendida=" + cantidadVendida;
        try {
            restTemplate.put(url, null);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                // Error 409: stock insuficiente
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    @SuppressWarnings("unchecked")
                    Map<String, String> errorBody = mapper.readValue(e.getResponseBodyAsString(), Map.class);
                    String mensaje = errorBody.getOrDefault("mensaje", "Stock agotado para este producto");
                    // Personalizar mensaje para ser más amigable
                    if (mensaje.contains("disponible=0")) {
                        throw new RuntimeException("Stock agotado. Este producto ya no está disponible");
                    }
                    throw new RuntimeException("Stock insuficiente. No hay suficientes unidades disponibles");
                } catch (RuntimeException re) {
                    throw re; // Re-lanzar RuntimeException personalizada
                } catch (Exception parseEx) {
                    throw new RuntimeException("Stock agotado para este producto");
                }
            }
            throw new RuntimeException("Error al actualizar stock del producto " + idProducto + ": " + e.getMessage());
        }
    }
    // Nuevo método para obtener todos los productos
    public ProductoDTO[] obtenerTodos() {
        String url = "http://localhost:8082/api/productos";
        return restTemplate.getForObject(url, ProductoDTO[].class);
    }
    


    // DTO interno para recibir datos del microservicio Producto
    public static class ProductoDTO {
        private Long idProducto;
        private String nombreProducto;
        private BigDecimal precioUnitario;
        private int stock;

        public Long getIdProducto() { return idProducto; }
        public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }

        public String getNombreProducto() { return nombreProducto; }
        public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

        public BigDecimal getPrecioUnitario() { return precioUnitario; }
        public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
    
        public int getStock() { return stock; }
        public void setStock(int stock) { this.stock = stock; }
    }

}