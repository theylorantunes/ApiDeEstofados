package com.estofados;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API Antunes Interiores",
                version = "1.0.0",
                description = "API RESTful comercial para consulta de catálogo, categorias e especificações de estofados de alto padrão. Requer chave de autenticação (X-API-Key). Limite do plano gratuito: 30 req/min.",
                contact = @Contact(name = "Suporte Antunes Interiores", email = "suporte@antunesinteriores.com", url = "https://antunesinteriores.com"),
                license = @License(name = "Termos de Uso", url = "/termos_de_servico.md")
        )
)
public class OpenApi { }