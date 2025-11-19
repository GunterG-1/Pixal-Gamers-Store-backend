package com.PixalStoreBackend.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.PixalStoreBackend.Usuario.model.Rol;
import com.PixalStoreBackend.Usuario.repository.RolRepository;

@SpringBootApplication
public class UsuarioApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsuarioApplication.class, args);
	}
	@Bean
    public CommandLineRunner initRoles(@Autowired RolRepository rolRepository) {
        return args -> {
            if (rolRepository.count() == 0) {
                rolRepository.save(new Rol(null, "ADMIN", null));
                rolRepository.save(new Rol(null, "CLIENTE", null));
            }
        };
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

