package com.medilabo.front.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationSuccessHandlerTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private JwtAuthenticationSuccessHandler handler;

    private UserDetails mockUserDetails;
    private final String mockJwt = "eyJhbGciOiJIUzI1NiJ9.mock-jwt";

    @Test
    void shouldGenerateJwtCookieAndRedirect_WhenValidUserDetails() throws Exception {
        mockUserDetails = mock(UserDetails.class);
        when(authentication.getPrincipal()).thenReturn(mockUserDetails);
        when(jwtService.generateToken(mockUserDetails)).thenReturn(mockJwt);

        handler.onAuthenticationSuccess(request, response, authentication);

        verify(jwtService).generateToken(mockUserDetails);
        verify(response).addCookie(argThat(cookie ->
                "JWT".equals(cookie.getName()) &&
                        mockJwt.equals(cookie.getValue()) &&
                        cookie.isHttpOnly() &&
                        cookie.getMaxAge() == 3600
        ));
        verify(response).sendRedirect("/");
        verifyNoInteractions(request);
    }

    @Test
    void shouldSendError_WhenPrincipalNotUserDetails() throws Exception {
        when(authentication.getPrincipal()).thenReturn("invalid-principal");

        handler.onAuthenticationSuccess(request, response, authentication);
        verify(response).sendError(500, "Invalid principal");
        verifyNoInteractions(jwtService);
        verifyNoMoreInteractions(response);
    }

    @Test
    void shouldSetCookiePropertiesCorrectly() throws Exception {
        mockUserDetails = mock(UserDetails.class);
        when(authentication.getPrincipal()).thenReturn(mockUserDetails);
        when(jwtService.generateToken(mockUserDetails)).thenReturn(mockJwt);

        handler.onAuthenticationSuccess(request, response, authentication);

        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(cookieCaptor.capture());
        Cookie cookie = cookieCaptor.getValue();

        assertThat(cookie.getName()).isEqualTo("JWT");
        assertThat(cookie.getValue()).isEqualTo(mockJwt);
        assertThat(cookie.isHttpOnly()).isTrue();
        assertThat(cookie.getSecure()).isFalse();
        assertThat(cookie.getMaxAge()).isEqualTo(3600);
    }

}
