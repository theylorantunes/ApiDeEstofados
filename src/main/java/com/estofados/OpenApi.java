package com.estofados;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API Antunes Interiores",
                version = "1.0",
                description = "API RESTful corporativa para gestão de catálogos de estofados sob medida.<br><br>" +
                        "<b>Funcionalidades e Arquitetura do Sistema:</b><ul>" +
                        "<li><b>Catálogo Relacional:</b> CRUD completo e paginado de Estofados, Categorias, Fabricantes, Materiais e Especificações Técnicas.</li>" +
                        "<li><b>Maturidade HATEOAS (Nível 3):</b> Respostas enriquecidas com hiperlinks (<code>_links</code>) para navegação autônoma entre os recursos.</li>" +
                        "<li><b>Segurança de Acesso:</b> Proteção de endpoints de negócio através de chave de autenticação (Header: <code>X-API-Key</code>).</li>" +
                        "<li><b>Rate Limiting (Anti-Spam):</b> Controle de tráfego restrito a 5 requisições por minuto utilizando Token Bucket.</li>" +
                        "<li><b>Idempotência Transacional:</b> Prevenção de duplicidade em criações (POST) através do cabeçalho <code>X-Idempotency-Key</code>, com sistema de cache de resposta.</li>" +
                        "<li><b>Versionamento de Contratos:</b> Suporte simultâneo à V1 (HATEOAS completo) e V2 (DTOs enxutos e aninhados) via cabeçalho <code>X-API-Version</code>.</li>" +
                        "<li><b>Blindagem de Dados:</b> Validação rigorosa de inputs (Bean Validation) e tratamento global de exceções (GlobalExceptionHandler) padronizando erros (400, 401, 404, 409, 429, 500).</li></ul>",
                contact = @Contact(name = "Suporte Antunes Interiores", email = "suporte@antunesinteriores.com")
        ),
        servers = {
                @Server(url = "https://apideestofados.onrender.com", description = "Servidor de Produção (Render)"),
                @Server(url = "http://localhost:8080", description = "Servidor Local (Desenvolvimento)")
        },
        security = @SecurityRequirement(name = "X-API-Key")
)
@SecurityScheme(
        name = "X-API-Key",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = "X-API-Key",
        description = "Insira a chave gerada no endpoint /auth/key"
)
public class OpenApi {
}