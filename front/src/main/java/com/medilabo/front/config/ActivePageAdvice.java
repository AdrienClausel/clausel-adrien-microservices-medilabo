package com.medilabo.front.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class ActivePageAdvice {

    @ModelAttribute("activePage")
    public String activePage(HttpServletRequest request){
        String uri = request.getRequestURI();

        if (uri.startsWith("/patients")) return "patients";
        if (uri.startsWith("/home")) return "home";
        if (uri.equals("/")) return "home";

        return "";
    }
}
