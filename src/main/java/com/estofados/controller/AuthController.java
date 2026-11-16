package com.estofados.controller;

import com.estofados.security.ApiKeyFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Gerenciamento de Chaves de API")
public class AuthController {

    @Operation(summary = "Gera uma nova chave de API (X-API-Key) para acesso")
    @PostMapping("/key")
    public ResponseEntity<String> gerarChave() {
        // Cria uma chave única e cadastra no nosso "banco" da memória
        String novaChave = "rubye-" + UUID.randomUUID().toString();
        ApiKeyFilter.chavesValidas.add(novaChave);

        return ResponseEntity.ok("Sua nova API Key é: " + novaChave + "\nEnvie isso no header 'X-API-Key' para usar a API.");
    }
}