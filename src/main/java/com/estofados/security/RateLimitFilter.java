package com.estofados.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
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
import java.util.concurrent.TimeUnit;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static final Duration WINDOW = Duration.ofMinutes(1);
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final Map<String, Bucket> authBuckets = new ConcurrentHashMap<>();
    private Bucket newBucket(long capacity) {
        Bandwidth limit = Bandwidth.classic(capacity, Refill.intervally(capacity, WINDOW));
        return Bucket.builder().addLimit(limit).build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String path = request.getRequestURI();

        if ("OPTIONS".equalsIgnoreCase(request.getMethod()) || path.startsWith("/swagger-") || path.startsWith("/api-docs") || path.startsWith("/v3/api-docs") || path.startsWith("/documentacao") || path.equals("/") || path.startsWith("/webjars") || path.endsWith(".css") || path.endsWith(".md")) {
            chain.doFilter(request, response);
            return;
        }


        if (path.startsWith("/auth/key")) {
            ConsumptionProbe probe = authBuckets.computeIfAbsent(clientIp(request), ip -> newBucket(2)).tryConsumeAndReturnRemaining(1);
            if (probe.isConsumed()) {
                chain.doFilter(request, response);
            } else {
                rejectRequest(response, probe);
            }
            return;
        }

        String apiKey = request.getHeader("X-API-Key");
        if (apiKey == null) apiKey = clientIp(request);

        ConsumptionProbe probe = buckets.computeIfAbsent(apiKey, ip -> newBucket(5)).tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            response.setHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            chain.doFilter(request, response);
        } else {
            rejectRequest(response, probe);
        }
    }

    private void rejectRequest(HttpServletResponse response, ConsumptionProbe probe) throws IOException {
        long retryAfter = TimeUnit.NANOSECONDS.toSeconds(probe.getNanosToWaitForRefill()) + 1;
        response.setStatus(429);
        response.setHeader("Retry-After", String.valueOf(retryAfter));
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"erro\":\"Muitas requisições, tente novamente em " + retryAfter + "s\"}");
    }

    private String clientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) return forwarded.split(",")[0].trim();
        return request.getRemoteAddr();
    }
}