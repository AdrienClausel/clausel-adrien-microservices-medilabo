package com.medilabo.front.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoResourceFoundException.class)
    public String handleNotFoundException(NoResourceFoundException ex, Model model) {
        log.error(ex.getMessage());
        model.addAttribute("status",404);
        model.addAttribute("message", "Ressource non trouvée");
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        log.error(ex.getMessage());
        model.addAttribute("status", 500);
        model.addAttribute("message", "Une erreur est survenue");
        return "error";
    }
}
