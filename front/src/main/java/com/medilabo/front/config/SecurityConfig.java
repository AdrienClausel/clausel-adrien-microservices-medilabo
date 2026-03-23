package com.medilabo.front.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configuration des chaines de filtre de sécurité
     * @param httpSecurity Objet permettant de configurer la sécurité
     * @return la chaine de filtres de sécurité configurée
     * @throws Exception Exception générée si une erreur arrive pendant la configuration
     */
    @Bean
    public SecurityFilterChain securityWebFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login","/css/**", "/js/**", "/images/**","/webjars/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
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
