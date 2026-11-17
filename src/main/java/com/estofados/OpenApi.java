package com.estofados;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API Antunes Interiores",
                version = "1.0",
                description = "API RESTful comercial para consulta de catálogo, categorias e especificações de estofados de alto padrão. Requer chave de autenticação (X-API-Key).",
                contact = @Contact(name = "Suporte Antunes Interiores", email = "suporte@antunesinteriores.com")
        ),
        servers = {
                @Server(url = "https://apideestofados.onrender.com", description = "Servidor de Produção (Render)"),
                @Server(url = "http://localhost:8080", description = "Servidor Local (Desenvolvimento)")
        }
)
public class OpenApi {
}