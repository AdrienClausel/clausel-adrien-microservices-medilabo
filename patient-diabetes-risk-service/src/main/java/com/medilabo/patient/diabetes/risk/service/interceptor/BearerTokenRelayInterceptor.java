package com.medilabo.patient.diabetes.risk.service.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

/**
 * Intercepteur chargé de relayer automatiquement le header
 * Authorization contenant un token Bearer vers les requêtes
 * sortantes (pour les appels au service patient et notes de patient)
 */
@Component
public class BearerTokenRelayInterceptor implements ClientHttpRequestInterceptor {

    /**
     * Intercepte une requête sortante et y ajoute le header Authorization
     * si la requête entrante en contient un.
     * @param request   requête HTTP sortante
     * @param body      corps de la requête
     * @param execution exécuteur permettant de poursuivre la chaîne d'interception
     * @return la réponse HTTP obtenue après exécution de la requête
     * @throws IOException en cas d'erreur lors de l'exécution de la requête
     */
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest currentRequest = attributes.getRequest();
            String auth = currentRequest.getHeader("Authorization");

            if (auth != null && auth.startsWith("Bearer ")) {
                request.getHeaders().set("Authorization", auth);
            }
        }

        return execution.execute(request, body);
    }
}
