package com.estofados.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private Bucket criarNovoBucket() {

        Bandwidth limit = Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limit).build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Não aplica limite no Swagger e rotas públicas
        if (path.startsWith("/swagger-ui") || path.startsWith("/api-docs") || path.startsWith("/v3/api-docs") ||
                path.startsWith("/documentacao") || path.equals("/") || path.startsWith("/auth/key") || path.startsWith("/webjars")) {
            filterChain.doFilter(request, response);
            return;
        }

        String apiKey = request.getHeader("X-API-Key");
        if (apiKey == null) {
            apiKey = request.getRemoteAddr(); // Usa o IP caso a chave não esteja presente (fallback)
        }

        // Recupera ou cria um bucket para o cliente atual
        Bucket bucket = buckets.computeIfAbsent(apiKey, k -> criarNovoBucket());

        if (bucket.tryConsume(1)) {
            // Requisição permitida, segue o fluxo
            filterChain.doFilter(request, response);
        } else {
            // Limite excedido: retorna 429 Too Many Requests
            response.setStatus(429);
            response.setHeader("Retry-After", "60"); // Informa para tentar novamente em 60 segundos
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"erro\": \"Limite de requisições excedido. Tente novamente mais tarde.\"}");
        }
    }
}