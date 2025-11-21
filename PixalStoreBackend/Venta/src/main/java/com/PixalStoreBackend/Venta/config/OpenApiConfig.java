package com.PixalStoreBackend.Venta.config;

import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info = @Info(title = "Venta API", version = "v1", description = "Operaciones de ventas, carrito y checkout"),
    servers = @Server(url = "/", description = "Servidor por defecto")
)
@Configuration
public class OpenApiConfig {
}
