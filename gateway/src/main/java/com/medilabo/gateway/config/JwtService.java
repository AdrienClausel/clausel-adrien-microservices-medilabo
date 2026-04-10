package com.medilabo.gateway.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Service responsable de la gestion et de la validation des tokens JWT.
 */
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    /**
     * Extrait le nom d'utilisateur (subject) contenu dans un token JWT.
     * @param token token JWT signé
     * @return le nom d'utilisateur
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Extrait l'ensemble des claims présents dans un token JWT.
     * Cette méthode vérifie la signature du token à l'aide de la clé HMAC
     * avant de retourner son contenu.
     * @param token token JWT signé
     * @return un objet Claims contenant toutes les informations du token
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Construit la clé secrète utilisée pour signer et vérifier les tokens JWT.
     * @return une clé HMAC valide pour la vérification des tokens
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extrait la date d'expiration d'un token JWT.
     * @param token token JWT signé
     * @return la date d'expiration du token
     */
    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    /**
     * Vérifie si un token JWT est expiré.
     * @param token token JWT signé
     * @return true si le token est expiré, false sinon
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Vérifie si un token JWT est valide.
     * @param token token JWT signé
     * @return true si le token est valide, false sinon
     */
    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.getSubject() != null && !isTokenExpired(token);
        } catch (JwtException e) {
            return false;
        }
    }
}
