package com.estofados;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Rubye Estofados API",
                version = "1.0.0",
                description = "API RESTful comercial para consulta de catálogo, categorias e especificações de estofados de alto padrão.",
                contact = @Contact(name = "Suporte Rubye", email = "api@rubye.com", url = "https://rubye.com/suporte"),
                license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0.html")
        )
)
public class OpenApi { }