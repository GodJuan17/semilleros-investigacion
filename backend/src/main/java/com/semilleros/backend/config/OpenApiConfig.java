package com.semilleros.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Semilleros de Investigación")
                        .version("1.0.0")
                        .description("API REST con arquitectura en capas para la gestión de Directores y Semilleros de Investigación.")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo")
                                .email("soporte@semilleros.local")));
    }
}

