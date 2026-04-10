package com.medilabo.front.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

/**
 * Intercepteur HTTP client chargé d'ajouter automatiquement le token JWT
 * présent dans les cookies de la requête entrante aux requêtes sortantes
 */
@Component
public class JwtHeaderInterceptor implements ClientHttpRequestInterceptor {

    /**
     * Intercepte une requête HTTP sortante et y ajoute le header Authorization
     * si un cookie JWT est présent dans la requête entrante.
     *
     * @param request   requête HTTP sortante
     * @param body      corps de la requête
     * @param execution exécuteur permettant de poursuivre la chaîne d'interception
     * @return la réponse HTTP obtenue après exécution de la requête
     * @throws IOException en cas d'erreur lors de l'exécution de la requête
     */
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest servletRequest = attributes.getRequest();

            if (servletRequest.getCookies() != null) {
                for (Cookie cookie : servletRequest.getCookies()) {
                    if ("JWT".equals(cookie.getName())) {
                        String jwt = cookie.getValue();
                        if (jwt != null && !jwt.isBlank()) {
                            request.getHeaders().setBearerAuth(jwt);
                        }
                    }
                }
            }
        }

        return execution.execute(request, body);
    }
}
