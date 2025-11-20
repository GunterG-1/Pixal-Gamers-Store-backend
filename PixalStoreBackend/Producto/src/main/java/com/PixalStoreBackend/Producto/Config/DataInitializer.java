package com.PixalStoreBackend.Producto.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.PixalStoreBackend.Producto.Model.Producto;
import com.PixalStoreBackend.Producto.Repository.ProductoRepository;

@Configuration
public class DataInitializer {

    private final Random random = new Random();

    @Bean
    CommandLineRunner seedProductos(ProductoRepository repo) {
        return args -> {
            
            List<Producto> toUpdate = new ArrayList<>();
            for (Producto p : repo.findAll()) {
                if (p.getStock() == null || p.getStock() == 0) {
                    p.setStock(randStock());
                    toUpdate.add(p);
                }
            }
            if (!toUpdate.isEmpty()) {
                repo.saveAll(toUpdate);
            }

          
            if (repo.count() == 0) {
                List<Producto> seeds = new ArrayList<>();

                seeds.add(build("Auriculares G335", 52990d,
                        "Auriculares gamer con sonido envolvente y micrófono integrado.",
                        "audio", "Auriculares", "/img/auricolare-removebg-preview.png"));
                seeds.add(build("Cable USB", 19990d,
                        "Cable USB tipo c de alta velocidad, ideal para periféricos.",
                        "accesorios", "Cable USB", "/img/cacle_usb-removebg-preview.png"));
                seeds.add(build("Figura Anime", 39990d,
                        "Figura anime del anime full metal alchemist brotherhood, edición limitada.",
                        "coleccionables", "Figura Anime", "/img/figura_anime-removebg-preview.png"));
                seeds.add(build("Hub USB", 14990d,
                        "Hub USB con múltiples puertos para conectar varios dispositivos.",
                        "accesorios", "Hub USB", "/img/hub_usb-removebg-preview.png"));
                seeds.add(build("hollow knight silksong", 5999d,
                        "Juego de aventura y acción para PC.",
                        "juegos", "Juego", "/img/juego1.png"));
                seeds.add(build("Micrófono", 24990d,
                        "Micrófono profesional para streaming y grabación.",
                        "audio", "Micrófono", "/img/microfono-removebg-preview.png"));
                seeds.add(build("Monitor", 159990d,
                        "Monitor gamer de alta definición y tasa de refresco elevada.",
                        "monitores", "Monitor", "/img/monitor-removebg-preview.png"));
                seeds.add(build("Mouse", 39990d,
                        "Mouse gamer ergonómico con iluminación RGB.",
                        "perifericos", "Mouse", "/img/mouse-removebg-preview.png"));
                seeds.add(build("Mouse Gamer", 34990d,
                        "Mouse gamer de alta precisión, ideal para eSports.",
                        "perifericos", "Mouse Gamer", "/img/nousegamer-removebg-preview.png"));
                seeds.add(build("Pad Mouse XL", 19990d,
                        "Pad Mouse XL, superficie amplia para movimientos precisos.",
                        "accesorios", "Pad Mouse XL", "/img/Pad_Mouse_XL-removebg-preview.png"));
                seeds.add(build("Soporte Monitor", 12990d,
                        "Soporte para monitor, mejora la ergonomía del escritorio.",
                        "accesorios", "Soporte Monitor", "/img/soporte_moni-removebg-preview.png"));
                seeds.add(build("Teclado Clásico", 24990d,
                        "Teclado clásico, resistente y cómodo para escribir.",
                        "perifericos", "Teclado Clásico", "/img/tacladoclasico-removebg-preview.png"));
                seeds.add(build("Teclado Gamer", 49990d,
                        "Teclado mecánico gamer con retroiluminación RGB.",
                        "perifericos", "Teclado Gamer", "/img/teclado-removebg-preview.png"));
                seeds.add(build("Webcam", 29990d,
                        "Webcam HD para videollamadas y streaming.",
                        "accesorios", "Webcam", "/img/webcam-removebg-preview.png"));

                repo.saveAll(seeds);
            }
        };
    }

    private Producto build(String nombre, Double precio, String descripcion,
                           String categoria, String categorialabel, String imagen) {
        Producto p = new Producto();
        p.setNombre(nombre);
        p.setPrecio(precio);
        p.setDescripcion(descripcion);
        p.setCategoria(categoria);
        p.setCategorialabel(categorialabel);
        p.setImagen(imagen);
        p.setDestacado(false);
        p.setStock(randStock());
        return p;
    }

    private int randStock() {
        return 1 + random.nextInt(50);
    }
}
