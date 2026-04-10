package com.medilabo.gateway.filter;

import com.medilabo.gateway.config.JwtService;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Filtre global qui valide les tokens JWT présents
 * dans les requêtes entrantes et le propage
 */
@Slf4j
@Component
public class JwtGatewayFilter implements GlobalFilter, Ordered {

    private final JwtService jwtService;

    public JwtGatewayFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Intercepte chaque requête transitant par le Gateway afin de vérifier
     * la validité du token JWT présent dans le header Authorization.
     * En cas de token manquant, invalide ou expiré, la requête est immédiatement
     * rejetée avec un statut 401 UNAUTHORIZED.
     * Si le token est valide, le filtre enrichit la requête avec :
     *  le header Authorization contenant le JWT,
     *  le header X-User-Name contenant le nom d'utilisateur extrait.
     * @param exchange l'échange HTTP contenant la requête et la réponse
     * @param chain    la chaîne de filtres du Gateway
     * @return un objet représentant l'exécution asynchrone du filtre
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String authHeader = request.getHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.error("Missing or invalid Authorization header: {}", authHeader);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String jwt = authHeader.substring(7);

        try {
            if (!jwtService.isTokenValid(jwt)) {
                log.error("JWT invalide ou expiré: {}", jwt);
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String username = jwtService.extractUsername(jwt);

            if (username == null || username.isBlank()) {
                log.error("JWT has no valid username: {}", jwt);
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            ServerHttpRequest modifiedRequest = request.mutate()
                    .header("Authorization", "Bearer " + jwt)
                    .header("X-User-Name", username)
                    .build();

            ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(modifiedRequest)
                    .build();

            return chain.filter(modifiedExchange);

        } catch (JwtException e) {
            log.error("JWT validation failed: {}", e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    /**
     * Définit l'ordre d'exécution du filtre dans la chaîne Spring Cloud Gateway.
     * Une valeur négative garantit une exécution précoce.
     * @return l'ordre du filtre
     */
    @Override
    public int getOrder() {
        return -100;
    }
}
