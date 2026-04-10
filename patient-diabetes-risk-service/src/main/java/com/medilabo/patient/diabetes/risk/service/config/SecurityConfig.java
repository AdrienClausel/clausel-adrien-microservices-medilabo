package com.medilabo.patient.diabetes.risk.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * Configuration de la sécurité pour le service, configuré en tant que
 * Resource Server OAuth2 utilisant des tokens JWT signés en HMAC (HS256).
 * Détails de la configuration :
 *  désactive la protection CSRF (inutile pour une API stateless),
 *  désactive la gestion de session au profit d'un mode STATELESS,
 *  exige que toutes les requêtes soient authentifiées,
 *  configure le serveur de ressources pour valider les JWT via un JwtDecoder personnalisé.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    /**
     * Configure la chaîne de filtres Spring Security pour un mode stateless.
     * @param httpSecurity l'objet de configuration HTTP
     * @return la chaîne de filtres configurée
     * @throws Exception en cas d'erreur lors de la construction de la configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.decoder(jwtDecoder()))
                );

        return httpSecurity.build();
    }

    /**
     * Crée un JwtDecoder basé sur une clé secrète HMAC (HS256).
     * La clé est dérivée de la propriété jwt.secret.
     * @return un décodeur JWT capable de vérifier les signatures HMAC
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        String algorithm = "HS256";
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec jwtKey = new SecretKeySpec(keyBytes, algorithm);
        return NimbusJwtDecoder
                .withSecretKey(jwtKey)
                .build();
    }
}
