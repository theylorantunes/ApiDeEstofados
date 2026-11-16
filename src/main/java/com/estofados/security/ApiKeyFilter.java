package com.estofados.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-API-Key";


    public static final Set<String> chavesValidas = new HashSet<>(Set.of("rubye-master-key-123"));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Libera rotas públicas: tela inicial, swagger e o endpoint de gerar chaves
        if (path.startsWith("/swagger-ui") || path.startsWith("/api-docs") || path.startsWith("/v3/api-docs") ||
                path.startsWith("/documentacao") || path.equals("/") || path.startsWith("/auth/key") || path.startsWith("/webjars")) {
            filterChain.doFilter(request, response);
            return;
        }

        String apiKey = request.getHeader(API_KEY_HEADER);

        if (apiKey == null || !chavesValidas.contains(apiKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"erro\": \"Acesso negado. Forneça um 'X-API-Key' válido no header.\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}