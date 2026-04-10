package com.medilabo.front.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration principale de Spring Security pour l'application.
 * Cette classe définit les règles de sécurité, la gestion de l'authentification,
 * la configuration du formulaire de connexion, ainsi que les paramètres de logout.
 * Elle active également l'infrastructure Spring Security
 * Lorsqu'un utilisateur s'authentifie avec succès, un token JWT est généré grâce
 * au JwtAuthenticationSuccessHandler, puis stocké dans un cookie sécurisé.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;

    /**
     * Constructeur injectant le handler responsable de la génération du JWT
     * après une authentification réussie.
     * @param jwtAuthenticationSuccessHandler handler de succès d'authentification
     */
    public SecurityConfig(JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler) {
        this.jwtAuthenticationSuccessHandler = jwtAuthenticationSuccessHandler;
    }

    /**
     * Configuration des chaines de filtre de sécurité
     * Utilisation d'un handler(jwtAuthenticationSuccessHandler) pour générer le token jwt
     * @param httpSecurity Objet permettant de configurer la sécurité
     * @return la chaine de filtres de sécurité configurée
     * @throws Exception Exception générée si une erreur arrive pendant la configuration
     */
    @Bean
    public SecurityFilterChain securityWebFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login","/css/**", "/js/**", "/images/**","/webjars/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(jwtAuthenticationSuccessHandler)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return httpSecurity.build();
    }

    /**
     * Définition d'un utilisateur en mémoire
     * @param bCryptPasswordEncoder Utilisation de l'encoder bCrypt pour crypter le mot de passe
     * @return Un objet utilisateur en mémoire (InMemoryUserDetailsManager)
     */
    @Bean
    public UserDetailsService userDetailsService(BCryptPasswordEncoder bCryptPasswordEncoder) {
        var user = User.withUsername("user")
                .password(bCryptPasswordEncoder.encode("1234"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

    /**
     * Utilisé pour configurer le chiffrement des mots de passe
     * @return Objet permettant d'encoder les mots de passe
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
