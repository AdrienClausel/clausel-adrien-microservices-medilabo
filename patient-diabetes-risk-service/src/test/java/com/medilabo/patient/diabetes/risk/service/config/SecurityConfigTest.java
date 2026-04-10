package com.medilabo.patient.diabetes.risk.service.config;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class SecurityConfigTest {

    private SecurityConfig securityConfig;

    private static final String VALID_SECRET = "une-cle-secrete-suffisamment-longue-pour-hs256";

    @BeforeEach
    void setUp() {
        securityConfig = new SecurityConfig();
        ReflectionTestUtils.setField(securityConfig, "jwtSecret", VALID_SECRET);
    }

    @Test
    void jwtDecoder_shouldDecodeValidToken() throws Exception {
        JwtDecoder decoder = securityConfig.jwtDecoder();
        String token = buildToken(VALID_SECRET, "user-42", future());

        Jwt jwt = decoder.decode(token);

        assertThat(jwt.getSubject()).isEqualTo("user-42");
    }

    @Test
    void jwtDecoder_shouldRejectTokenSignedWithWrongKey() throws Exception {
        JwtDecoder decoder = securityConfig.jwtDecoder();
        String token = buildToken("mauvaise-cle-qui-ne-correspond-pas-du-tout!", "hacker", future());

        assertThatThrownBy(() -> decoder.decode(token))
                .isInstanceOf(JwtException.class);
    }

    @Test
    void jwtDecoder_shouldRejectExpiredToken() throws Exception {
        JwtDecoder decoder = securityConfig.jwtDecoder();
        String token = buildToken(VALID_SECRET, "user-42", past());

        assertThatThrownBy(() -> decoder.decode(token))
                .isInstanceOf(JwtException.class);
    }

    @Test
    void jwtDecoder_shouldRejectMalformedToken() {
        JwtDecoder decoder = securityConfig.jwtDecoder();

        assertThatThrownBy(() -> decoder.decode("ceci.nest.pas.un.jwt"))
                .isInstanceOf(JwtException.class);
    }

    @Test
    void jwtDecoder_shouldFailWhenSecretIsEmpty() {
        ReflectionTestUtils.setField(securityConfig, "jwtSecret", "");

        assertThatThrownBy(() -> securityConfig.jwtDecoder())
                .isInstanceOf(Exception.class);
    }

    private String buildToken(String secret, String subject, Date expiry) throws Exception {
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(subject)
                .issueTime(new Date())
                .expirationTime(expiry)
                .build();

        SignedJWT jwt = new SignedJWT(
                new JWSHeader(JWSAlgorithm.HS256),
                claims
        );
        jwt.sign(new MACSigner(secret.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
        return jwt.serialize();
    }

    private Date future() {
        return new Date(System.currentTimeMillis() + 3_600_000);
    }

    private Date past() {
        return new Date(System.currentTimeMillis() - 3_600_000);
    }

}
