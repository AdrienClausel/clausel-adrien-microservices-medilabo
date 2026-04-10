package com.medilabo.front.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Contrôleur gérant les pages d'accueil de l'application.
 */
@Controller
public class HomeController {

    /**
     * Redirige la racine de l'application vers la page d'accueil.
     * @return une redirection vers /home
     */
    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    /**
     * Affiche la page d'accueil de l'application.
     * @return le nom de la vue home
     */
    @GetMapping("/home")
    public String home() {
        return "home";
    }
}
