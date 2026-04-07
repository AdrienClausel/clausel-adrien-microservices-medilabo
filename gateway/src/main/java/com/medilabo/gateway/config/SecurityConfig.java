package com.medilabo.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Configuration de la sécurité
 * activation de la sécurité et configuration d'une chaine de filtres de sécurité
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    /**
     * Configure la chaine de filtres de sécurité pour les requêtes Http
     * Désactive la protection CSRF
     * Autorise toutes les requêtes sans authentification
     * @param http objet utilisé pour configurer la sécurité
     * @return chaine de filtres de sécurité
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(exchanges -> exchanges
                        .anyExchange().permitAll()
                )
                .build();
    }
}
