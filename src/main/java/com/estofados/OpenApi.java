package com.estofados;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API Antunes Interiores",
                version = "1.0.0",
                description = "API RESTful comercial para consulta de catálogo, categorias e especificações de estofados de alto padrão. Requer chave de autenticação (X-API-Key). Limite do plano gratuito: 30 req/min.",
                contact = @Contact(name = "Suporte Antunes Interiores", email = "suporte@antunesinteriores.com", url = "https://antunes-interiores.vercel.app"),
                license = @License(name = "Termos de Uso", url = "/termos_de_servico.md")
        ),
        security = @SecurityRequirement(name = "ApiKeyAuth") // Aplica a trava globalmente no Swagger
)
@SecurityScheme(
        name = "ApiKeyAuth",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = "X-API-Key" // Nome exato do header que o professor pediu
)
public class OpenApi { }