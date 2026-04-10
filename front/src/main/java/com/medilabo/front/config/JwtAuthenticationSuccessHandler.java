package com.medilabo.front.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Gestionnaire d'authentification Spring Security chargé de traiter les succès
 * de connexion et de générer un JWT pour l'utilisateur authentifié.
 * Lorsqu'une authentification réussit, ce handler :
 *  vérifie que le principal est bien une instance de UserDetails,
 *  génère un token JWT,
 *  stocke ce token dans un cookie HTTP sécurisé,
 *  et redirige l'utilisateur vers la page d'accueil
 * Le cookie JWT est configuré avec ces paramètres:
 *  empêche l'accès via JavaScript,
 *  le sécurise en fonction d'un paramètre (false pour le dev puis true pour la prod),
 *  la durée de vie est de 1h
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Value("${jwt.cookie.secure}")
    private boolean cookieSecure;

    /**
     * Traite un succès d'authentification en générant un JWT et en le stockant
     * dans un cookie sécurisé, puis en redirigeant l'utilisateur.
     *
     * @param request        requête HTTP entrante
     * @param response       réponse HTTP sortante
     * @param authentication objet d'authentification contenant l'utilisateur connecté
     * @throws IOException      en cas d'erreur d'écriture dans la réponse
     * @throws ServletException en cas d'erreur liée au traitement servlet
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails userDetails)) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Invalid principal");
            return;
        }

        String jwt = jwtService.generateToken(userDetails);

        Cookie cookie = new Cookie("JWT", jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(cookieSecure);
        cookie.setMaxAge(3600);
        response.addCookie(cookie);

        response.sendRedirect("/");
    }
}
