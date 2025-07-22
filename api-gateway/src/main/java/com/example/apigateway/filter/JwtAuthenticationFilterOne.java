package com.example.apigateway.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.jwt.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilterOne implements WebFilter {

    private static final Logger logger = LogManager.getLogger(JwtAuthenticationFilterOne.class);

    private static final String[] PERMITTED_PATHS = {
            "/h2-console/**",
            "/actuator/**",
            "/api/users/login",
            "/api/users/register",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/v3/api-docs",
            "/webjars/**"
    };


    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilterOne(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();
        String method = exchange.getRequest().getMethod().name();
        logger.info("Incoming request: {} {} from {}", method, path,
                exchange.getRequest().getRemoteAddress());

        for (String permittedPath : PERMITTED_PATHS) {
            if (path.startsWith(permittedPath.replace("/**", ""))) {
                logger.debug("Skipping JWT validation for permitted path: {}", path);
                return chain.filter(exchange)
                        .doOnSuccess(v -> logSuccess(exchange, "Permitted path access"));
            }
        }
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("Missing or invalid Authorization header for path: {}", path);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return chain.filter(exchange)
                    .doOnSuccess(v -> logErrorResponse(exchange, "Missing token"));
        }

        String token = authHeader.substring(7);
        try {
            jwtUtil.validateToken(token);
            logger.debug("Valid JWT token for path: {}", path);
            return chain.filter(exchange)
                    .doOnSuccess(v -> logSuccess(exchange, "Authenticated access"))
                    .doOnError(e -> logError(exchange, e));
        } catch (Exception e) {
            logger.error("JWT validation failed for path {}: {}", path, e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return chain.filter(exchange)
                    .doOnSuccess(v -> logErrorResponse(exchange, "Invalid token"));
        }
    }

    private void logSuccess(ServerWebExchange exchange, String message) {
        logger.info("Request completed: {} {} - Status: {} - {}",
                exchange.getRequest().getMethod().name(),
                exchange.getRequest().getPath(),
                exchange.getResponse().getStatusCode(),
                message);
    }

    private void logError(ServerWebExchange exchange, Throwable e) {
        logger.error("Request failed: {} {} - Error: {}",
                exchange.getRequest().getMethod().name(),
                exchange.getRequest().getPath(),
                e.getMessage());
    }

    private void logErrorResponse(ServerWebExchange exchange, String reason) {
        logger.warn("Request denied: {} {} - Reason: {}",
                exchange.getRequest().getMethod().name(),
                exchange.getRequest().getPath(),
                reason);
    }
}