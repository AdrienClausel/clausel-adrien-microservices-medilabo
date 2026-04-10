package com.medilabo.front.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service responsable de la génération des tokens JWT.
 * Cette classe encapsule les opérations principales :
 *  génération de tokens signés contenant l'identité et les rôles d'un utilisateur,
 *  extraction du nom d'utilisateur depuis un token,
 *  extraction de l'ensemble des claims (informations encodées dans le token),
 *  gestion de la clé de signature HMAC (algorithme de signature).
 */
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    /**
     * Génère un token JWT pour l'utilisateur donné, sans claims supplémentaires.
     * @param userDetails informations de l'utilisateur authentifié
     * @return un token JWT signé
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails, Map.of());
    }

    /**
     * Génère un token JWT pour l'utilisateur donné, en ajoutant des claims personnalisés.
     * Le token contient notamment :
     *  le nom d'utilisateur (subject),
     *  la liste des rôles de l'utilisateur,
     *  la date d'émission,
     *  la date d'expiration,
     *  les claims supplémentaires fournis.
     * @param userDetails informations de l'utilisateur authentifié
     * @param extraClaims claims additionnels à inclure dans le token
     * @return un token JWT signé
     */
    public String generateToken(UserDetails userDetails, Map<String, Object> extraClaims) {
        long now = System.currentTimeMillis();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .claim("roles", roles)
                .issuedAt(new Date(now))
                .expiration(new Date(now + expirationMs))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    /**
     * Extrait le nom d'utilisateur (subject) depuis un token JWT.
     * @param token token JWT signé
     * @return le nom d'utilisateur contenu dans le token
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Extrait l'ensemble des claims présents dans un token JWT.
     * Cette méthode vérifie la signature du token avant de retourner son contenu.
     * @param token token JWT signé
     * @return les claims du token sous forme de {@link Claims}
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
     * @return une clé HMAC valide pour l'algorithme HS256
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
