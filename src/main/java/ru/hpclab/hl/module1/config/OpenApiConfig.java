package ru.hpclab.hl.module1.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI mainServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Farmers Market Main Service API")
                        .description("API для управления данными фермерского рынка")
                        .version("1.0")
                        .contact(new Contact()
                                .name("HPCLab")
                                .email("contact@hpclab.ru")));
    }
} 