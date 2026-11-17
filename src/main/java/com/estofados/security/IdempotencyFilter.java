package com.estofados.security;

import com.estofados.config.ApiVersionConfig;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class IdempotencyFilter extends OncePerRequestFilter {

    public static final String HEADER = "X-Idempotency-Key";
    private static final int MAX_KEY_LENGTH = 255;
    private static final Duration TTL = Duration.ofHours(24);

    private record Entry(byte[] requestHash, Instant createdAt, StoredResponse response) {
        boolean expired() { return createdAt.plus(TTL).isBefore(Instant.now()); }
    }
    private record StoredResponse(int status, String contentType, String location, byte[] body) {}

    private final Map<String, Entry> entries = new ConcurrentHashMap<>();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !"POST".equalsIgnoreCase(request.getMethod()) || request.getHeader(HEADER) == null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String key = request.getHeader(HEADER).trim();
        if (key.isEmpty() || key.length() > MAX_KEY_LENGTH) {
            writeError(response, 400, "X-Idempotency-Key deve ter entre 1 e 255 caracteres");
            return;
        }
        String storeKey = request.getRequestURI() + "|" + apiVersion(request) + "|" + key;

        Entry existing = entries.get(storeKey);
        if (existing != null && existing.expired()) {
            entries.remove(storeKey, existing);
            existing = null;
        }

        if (existing != null) {
            byte[] hash = sha256(request.getInputStream().readAllBytes());
            if (!Arrays.equals(hash, existing.requestHash())) {
                writeError(response, 422, "Chave de Idempotência já utilizada com outro corpo de requisição");
            } else if (existing.response() == null) {
                writeError(response, 409, "Requisição com esta chave ainda está em processamento");
            } else {
                replay(response, existing.response());
            }
            return;
        }

        byte[] body = request.getInputStream().readAllBytes();
        Entry inProgress = new Entry(sha256(body), Instant.now(), null);
        Entry raced = entries.putIfAbsent(storeKey, inProgress);

        if (raced != null) {
            writeError(response, 409, "Requisição com esta chave ainda está em processamento");
            return;
        }
        evictExpired();

        ContentCachingResponseWrapper cachedResponse = new ContentCachingResponseWrapper(response);
        boolean stored = false;
        try {
            chain.doFilter(new BodyReplayingRequest(request, body), cachedResponse);
            int status = cachedResponse.getStatus();
            if (status >= 200 && status < 300) {
                StoredResponse result = new StoredResponse(status, cachedResponse.getContentType(), cachedResponse.getHeader(HttpHeaders.LOCATION), cachedResponse.getContentAsByteArray());
                entries.replace(storeKey, inProgress, new Entry(inProgress.requestHash(), inProgress.createdAt(), result));
                stored = true;
            }
        } finally {
            if (!stored) entries.remove(storeKey, inProgress);
            cachedResponse.copyBodyToResponse();
        }
    }

    private void replay(HttpServletResponse response, StoredResponse stored) throws IOException {
        response.setStatus(stored.status());
        if (stored.contentType() != null) response.setContentType(stored.contentType());
        if (stored.location() != null) response.setHeader(HttpHeaders.LOCATION, stored.location());
        response.setHeader("Idempotent-Replayed", "true");
        response.getOutputStream().write(stored.body());
    }

    private static String apiVersion(HttpServletRequest request) {
        String version = request.getHeader(ApiVersionConfig.HEADER);
        if (version == null) version = request.getParameter(ApiVersionConfig.QUERY_PARAM);
        return version == null ? "default" : version.trim();
    }

    private void evictExpired() { entries.values().removeIf(entry -> entry.response() != null && entry.expired()); }
    private static void writeError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status); response.setContentType("application/json"); response.getWriter().write("{\"erro\":\"" + message + "\"}");
    }
    private static byte[] sha256(byte[] data) {
        try { return MessageDigest.getInstance("SHA-256").digest(data); } catch (NoSuchAlgorithmException e) { throw new IllegalStateException(e); }
    }

    private static class BodyReplayingRequest extends HttpServletRequestWrapper {
        private final byte[] body;
        BodyReplayingRequest(HttpServletRequest request, byte[] body) { super(request); this.body = body; }
        @Override
        public ServletInputStream getInputStream() {
            ByteArrayInputStream in = new ByteArrayInputStream(body);
            return new ServletInputStream() {
                @Override public int read() { return in.read(); }
                @Override public int read(byte[] b, int off, int len) { return in.read(b, off, len); }
                @Override public boolean isFinished() { return in.available() == 0; }
                @Override public boolean isReady() { return true; }
                @Override public void setReadListener(ReadListener listener) { throw new UnsupportedOperationException(); }
            };
        }
        @Override public BufferedReader getReader() { return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8)); }
    }
}