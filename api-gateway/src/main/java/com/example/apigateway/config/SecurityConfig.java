package com.example.apigateway.config;

import com.example.apigateway.filter.JwtAuthenticationFilterOne;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
        private final JwtAuthenticationFilterOne jwtAuthenticationFilter;

        public SecurityConfig(JwtAuthenticationFilterOne jwtAuthenticationFilter) {
            this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        }

        @Bean
        public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
            return http
                    .csrf(ServerHttpSecurity.CsrfSpec::disable)
                    .authorizeExchange(exchanges -> exchanges
                            .pathMatchers(
                                    "/h2-console/**",
                                    "/actuator/**",
                                    "/api/users/login",
                                    "/api/users/register"
                            ).permitAll()
                            .anyExchange().authenticated()
                    )
                    .addFilterBefore(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                    .build();
        }
    }