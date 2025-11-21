package com.PixalStoreBackend.Usuario.config;

import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info = @Info(title = "Usuario API", version = "v1", description = "Operaciones de usuarios, roles y autenticación"),
    servers = @Server(url = "/", description = "Servidor por defecto")
)
@Configuration
public class OpenApiConfig {
}
