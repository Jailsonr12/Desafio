package com.jailson.hotel.config;

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
                        .title("Defaio Hotel API")
                        .version("0.0.1")
                        .description("API para gestão de check-ins e hóspedes")
                        .contact(new Contact().name("Jailson").email(""))
                );
    }
}

