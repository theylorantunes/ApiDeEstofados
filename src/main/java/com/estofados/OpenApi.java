package com.estofados;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApi {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestão de Estofados")
                        .description("Documentação interativa da API REST para gestão de estofados, fabricantes, materiais e especificações. Desenvolvida para a disciplina de Sistemas para Internet.")
                        .version("1.0.0"));
    }
}