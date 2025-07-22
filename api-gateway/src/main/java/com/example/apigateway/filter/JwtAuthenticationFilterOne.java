package com.example.apigateway.filter;

import org.example.jwt.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilterOne implements WebFilter {


    private static final String[] PERMITTED_PATHS = {
            "/h2-console/**",
            "/actuator/**",
            "/api/users/login",
            "/api/users/register"
    };
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilterOne(JwtUtil jwtUtil ) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
            String path = exchange.getRequest().getPath().toString();
        for (String permittedPath : PERMITTED_PATHS) {
            if (path.startsWith(permittedPath.replace("/**", ""))) {
                return chain.filter(exchange);
            }
        }
            String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            String token = authHeader.substring(7);
            try {
                jwtUtil.validateToken(token);
                return chain.filter(exchange);
            } catch (Exception e) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        }
    }