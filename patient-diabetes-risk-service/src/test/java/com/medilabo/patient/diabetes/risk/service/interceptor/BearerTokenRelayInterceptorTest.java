package com.medilabo.patient.diabetes.risk.service.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BearerTokenRelayInterceptorTest {

    private BearerTokenRelayInterceptor interceptor;

    @BeforeEach
    void setup() {
        interceptor = new BearerTokenRelayInterceptor();
    }

    @Test
    void shouldRelayBearerTokenWhenPresent() throws IOException {
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.getHeader("Authorization")).thenReturn("Bearer abc123");

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(servletRequest));

        HttpRequest httpRequest = mock(HttpRequest.class);
        HttpHeaders headers = new HttpHeaders();
        when(httpRequest.getHeaders()).thenReturn(headers);

        ClientHttpRequestExecution execution = mock(ClientHttpRequestExecution.class);
        ClientHttpResponse response = mock(ClientHttpResponse.class);
        when(execution.execute(any(), any())).thenReturn(response);

        ClientHttpResponse result = interceptor.intercept(httpRequest, new byte[0], execution);

        assertThat(headers.getFirst("Authorization")).isEqualTo("Bearer abc123");
        assertThat(result).isSameAs(response);

        verify(execution).execute(httpRequest, new byte[0]);
    }

    @Test
    void shouldNotSetAuthorizationHeaderWhenMissing() throws IOException {
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.getHeader("Authorization")).thenReturn(null);

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(servletRequest));

        HttpRequest httpRequest = mock(HttpRequest.class);
        HttpHeaders headers = new HttpHeaders();
        when(httpRequest.getHeaders()).thenReturn(headers);

        ClientHttpRequestExecution execution = mock(ClientHttpRequestExecution.class);
        ClientHttpResponse response = mock(ClientHttpResponse.class);
        when(execution.execute(any(), any())).thenReturn(response);

        interceptor.intercept(httpRequest, new byte[0], execution);

        assertThat(headers.containsKey("Authorization")).isFalse();
    }

    @Test
    void shouldNotRelayInvalidAuthorizationHeader() throws IOException {
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.getHeader("Authorization")).thenReturn("Basic xyz");

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(servletRequest));

        HttpRequest httpRequest = mock(HttpRequest.class);
        HttpHeaders headers = new HttpHeaders();
        when(httpRequest.getHeaders()).thenReturn(headers);

        ClientHttpRequestExecution execution = mock(ClientHttpRequestExecution.class);
        ClientHttpResponse response = mock(ClientHttpResponse.class);
        when(execution.execute(any(), any())).thenReturn(response);

        interceptor.intercept(httpRequest, new byte[0], execution);

        assertThat(headers.containsKey("Authorization")).isFalse();
    }
}