package com.medilabo.gateway.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;
    private final String secret = "mySecretKeyForJwtWhichIsLongEnoughForHmacSha256";
    private final long expirationMs = 3600000;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "secret", secret);
        ReflectionTestUtils.setField(jwtService, "expirationMs", expirationMs);
    }

    private String generateTestToken(String subject) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key)
                .compact();
    }

    private String generateExpiredToken(String subject) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis() - expirationMs - 1000))
                .expiration(new Date(System.currentTimeMillis() - expirationMs))
                .signWith(key)
                .compact();
    }

    @Test
    void extractUsername_ValidToken_ReturnsSubject() {
        String subject = "testUser";
        String token = generateTestToken(subject);
        String username = jwtService.extractUsername(token);
        assertEquals(subject, username);
    }

    @Test
    void extractUsername_InvalidToken_ThrowsException() {
        String invalidToken = "invalid.token.here";
        assertThrows(JwtException.class, () -> jwtService.extractUsername(invalidToken));
    }

    @Test
    void extractAllClaims_ValidToken_ReturnsClaims() {
        String token = generateTestToken("testUser");
        Claims claims = jwtService.extractAllClaims(token);
        assertNotNull(claims);
        assertEquals("testUser", claims.getSubject());
    }

    @Test
    void extractAllClaims_InvalidToken_ThrowsException() {
        String invalidToken = "invalid.token.here";
        assertThrows(JwtException.class, () -> jwtService.extractAllClaims(invalidToken));
    }

    @Test
    void extractExpiration_ValidToken_ReturnsExpirationDate() {
        String token = generateTestToken("testUser");
        Date expiration = jwtService.extractExpiration(token);
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void isTokenExpired_NotExpiredToken_ReturnsFalse() {
        String token = generateTestToken("testUser");
        boolean isExpired = jwtService.isTokenExpired(token);
        assertFalse(isExpired);
    }

    @Test
    void isTokenExpired_ExpiredToken_ReturnsTrue() {
        String token = generateExpiredToken("testUser");

        try {
            boolean isExpired = jwtService.isTokenExpired(token);
            assertTrue(isExpired);
        } catch (JwtException e) {
            assertInstanceOf(ExpiredJwtException.class, e);
        }
    }

    @Test
    void isTokenValid_ValidToken_ReturnsTrue() {
        String token = generateTestToken("testUser");
        boolean isValid = jwtService.isTokenValid(token);
        assertTrue(isValid);
    }

    @Test
    void isTokenValid_ExpiredToken_ReturnsFalse() {
        String token = generateExpiredToken("testUser");
        boolean isValid = jwtService.isTokenValid(token);
        assertFalse(isValid);
    }

    @Test
    void isTokenValid_InvalidToken_ReturnsFalse() {
        String invalidToken = "invalid.token.here";
        boolean isValid = jwtService.isTokenValid(invalidToken);
        assertFalse(isValid);
    }

    @Test
    void isTokenValid_TokenWithoutSubject_ReturnsFalse() {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        String token = Jwts.builder()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key)
                .compact();
        boolean isValid = jwtService.isTokenValid(token);
        assertFalse(isValid);
    }

}
