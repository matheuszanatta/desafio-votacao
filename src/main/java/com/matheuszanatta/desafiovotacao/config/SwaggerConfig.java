package com.matheuszanatta.desafiovotacao.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Desafio Votação API")
                                .version("1.0.0")
                                .description("API para gerenciamento de votações em pautas")
                                .contact(new Contact()
                                        .name("Matheus Zanatta")
                                        .email("matheus.zanatta.nh@gmail.com")
                                        .url("https://github.com/matheuszanatta"))
                );
    }
}
