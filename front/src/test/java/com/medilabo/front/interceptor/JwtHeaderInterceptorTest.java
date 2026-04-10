package com.medilabo.front.interceptor;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtHeaderInterceptorTest {

    @Mock
    private ClientHttpRequestExecution execution;

    @Mock
    private ClientHttpResponse response;

    @InjectMocks
    private JwtHeaderInterceptor interceptor;

    private HttpRequest mockRequest;
    private ServletRequestAttributes servletAttributes;
    private MockHttpServletRequest servletRequest;

    @BeforeEach
    void setUp() {
        mockRequest = mock(HttpRequest.class);
        servletRequest = new MockHttpServletRequest();
        servletAttributes = new ServletRequestAttributes(servletRequest);

        HttpHeaders headers = new HttpHeaders();
        when(mockRequest.getHeaders()).thenReturn(headers);
    }

    @Test
    void shouldAddBearerToken_WhenValidJwtCookiePresent() throws IOException {
        String jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIn0.test";
        servletRequest.setCookies(new Cookie("JWT", jwtToken));

        try (MockedStatic<RequestContextHolder> mockedStatic = Mockito.mockStatic(RequestContextHolder.class)) {
            mockedStatic.when(RequestContextHolder::getRequestAttributes).thenReturn(servletAttributes);
            when(execution.execute(eq(mockRequest), any(byte[].class))).thenReturn(response);

            interceptor.intercept(mockRequest, new byte[0], execution);

            assertThat(mockRequest.getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                    .isEqualTo("Bearer " + jwtToken);
        }
    }

    @Test
    void shouldNotAddHeader_WhenNoRequestContext() throws IOException {
        try (MockedStatic<RequestContextHolder> mockedStatic = Mockito.mockStatic(RequestContextHolder.class)) {

            mockedStatic.when(RequestContextHolder::getRequestAttributes).thenReturn(null);

            when(execution.execute(eq(mockRequest), any(byte[].class))).thenReturn(response);

            interceptor.intercept(mockRequest, new byte[0], execution);

            assertThat(mockRequest.getHeaders().getFirst(HttpHeaders.AUTHORIZATION)).isNull();
        }
    }

    @Test
    void shouldNotAddHeader_WhenNoJwtCookie() throws IOException {
        try (MockedStatic<RequestContextHolder> mockedStatic = Mockito.mockStatic(RequestContextHolder.class)) {

            mockedStatic.when(RequestContextHolder::getRequestAttributes).thenReturn(servletAttributes);

            when(execution.execute(eq(mockRequest), any(byte[].class))).thenReturn(response);

            interceptor.intercept(mockRequest, new byte[0], execution);

            assertThat(mockRequest.getHeaders().getFirst(HttpHeaders.AUTHORIZATION)).isNull();
        }
    }


    private MockedStatic<RequestContextHolder> mockRequestContext(RequestAttributes attributes) {
        return mockStatic(RequestContextHolder.class);
    }
}
