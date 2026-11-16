package com.estofados.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class IdempotencyFilter extends OncePerRequestFilter {

    private static final String IDEMPOTENCY_HEADER = "X-Idempotency-Key";

    private final Set<String> chavesProcessadas = ConcurrentHashMap.newKeySet();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();


        if ("POST".equalsIgnoreCase(request.getMethod()) && !path.startsWith("/v1/auth/key")) {
            String idempotencyKey = request.getHeader(IDEMPOTENCY_HEADER);


            if (idempotencyKey == null || idempotencyKey.isBlank()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"erro\": \"O cabeçalho 'X-Idempotency-Key' é obrigatório para operações POST.\"}");
                return;
            }


            if (!chavesProcessadas.add(idempotencyKey)) {
                response.setStatus(HttpServletResponse.SC_CONFLICT); // 409
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"erro\": \"Operação já realizada. Esta requisição foi ignorada para evitar duplicidade.\"}");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}