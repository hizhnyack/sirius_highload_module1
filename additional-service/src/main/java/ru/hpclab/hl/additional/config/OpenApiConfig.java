package ru.hpclab.hl.additional.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI additionalServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Farmers Market Additional Service API")
                        .description("API для расчета среднего веса закупки по каждому наименованию товара")
                        .version("1.0")
                        .contact(new Contact()
                                .name("HPCLab")
                                .email("contact@hpclab.ru")));
    }
} 