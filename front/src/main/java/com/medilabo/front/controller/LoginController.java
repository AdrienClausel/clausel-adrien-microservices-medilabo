package com.medilabo.front.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Contrôleur responsable de l'affichage de la page de connexion.
 */
@Controller
public class LoginController {

    /**
     * Affiche la page de connexion de l'application.
     * @return le nom de la vue login
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
