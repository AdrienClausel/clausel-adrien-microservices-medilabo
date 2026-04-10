package com.medilabo.gateway.filter;

import com.medilabo.gateway.config.JwtService;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtGatewayFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private GatewayFilterChain chain;

    @InjectMocks
    private JwtGatewayFilter jwtGatewayFilter;

    private ServerWebExchange exchange;
    private ServerHttpRequest request;

    @BeforeEach
    void setUp() {
        request = MockServerHttpRequest.get("/test").build();
        exchange = MockServerWebExchange.from((MockServerHttpRequest) request);
    }

    @Test
    void filter_MissingAuthHeader_ReturnsUnauthorized() {
        //when(chain.filter(any())).thenReturn(Mono.empty());

        Mono<Void> result = jwtGatewayFilter.filter(exchange, chain);

        StepVerifier.create(result)
                .verifyComplete();

        assert exchange.getResponse().getStatusCode() == HttpStatus.UNAUTHORIZED;
    }

    @Test
    void filter_InvalidAuthHeader_ReturnsUnauthorized() {
        request = MockServerHttpRequest.get("/test")
                .header("Authorization", "InvalidToken")
                .build();
        exchange = MockServerWebExchange.from((MockServerHttpRequest) request);

        Mono<Void> result = jwtGatewayFilter.filter(exchange, chain);

        StepVerifier.create(result)
                .verifyComplete();

        assert exchange.getResponse().getStatusCode() == HttpStatus.UNAUTHORIZED;
    }

    @Test
    void filter_InvalidJwt_ReturnsUnauthorized() {
        request = MockServerHttpRequest.get("/test")
                .header("Authorization", "Bearer invalid.jwt.token")
                .build();
        exchange = MockServerWebExchange.from((MockServerHttpRequest) request);

        when(jwtService.isTokenValid(anyString())).thenReturn(false);

        Mono<Void> result = jwtGatewayFilter.filter(exchange, chain);

        StepVerifier.create(result)
                .verifyComplete();

        assert exchange.getResponse().getStatusCode() == HttpStatus.UNAUTHORIZED;
    }

    @Test
    void filter_JwtException_ReturnsUnauthorized() {
        request = MockServerHttpRequest.get("/test")
                .header("Authorization", "Bearer invalid.jwt.token")
                .build();
        exchange = MockServerWebExchange.from((MockServerHttpRequest) request);

        when(jwtService.isTokenValid(anyString())).thenThrow(new JwtException("Invalid JWT"));

        Mono<Void> result = jwtGatewayFilter.filter(exchange, chain);

        StepVerifier.create(result)
                .verifyComplete();

        assert exchange.getResponse().getStatusCode() == HttpStatus.UNAUTHORIZED;
    }

    @Test
    void filter_ValidJwtButNoUsername_ReturnsUnauthorized() {
        request = MockServerHttpRequest.get("/test")
                .header("Authorization", "Bearer valid.jwt.token")
                .build();
        exchange = MockServerWebExchange.from((MockServerHttpRequest) request);

        when(jwtService.isTokenValid(anyString())).thenReturn(true);
        when(jwtService.extractUsername(anyString())).thenReturn("");

        Mono<Void> result = jwtGatewayFilter.filter(exchange, chain);

        StepVerifier.create(result)
                .verifyComplete();

        assert exchange.getResponse().getStatusCode() == HttpStatus.UNAUTHORIZED;
    }

    @Test
    void filter_ValidJwtAndUsername_ContinuesChain() {
        request = MockServerHttpRequest.get("/test")
                .header("Authorization", "Bearer valid.jwt.token")
                .build();
        exchange = MockServerWebExchange.from((MockServerHttpRequest) request);

        when(jwtService.isTokenValid(anyString())).thenReturn(true);
        when(jwtService.extractUsername(anyString())).thenReturn("testUser");
        when(chain.filter(any())).thenReturn(Mono.empty());

        Mono<Void> result = jwtGatewayFilter.filter(exchange, chain);

        StepVerifier.create(result)
                .verifyComplete();

        ArgumentCaptor<ServerWebExchange> exchangeCaptor = ArgumentCaptor.forClass(ServerWebExchange.class);
        verify(chain).filter(exchangeCaptor.capture());

        ServerHttpRequest capturedRequest = exchangeCaptor.getValue().getRequest();

        assert capturedRequest.getHeaders().containsKey("X-User-Name");
        assert capturedRequest.getHeaders().getFirst("X-User-Name").equals("testUser");
    }
}
