package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.security.jwt.JwtAuthEntryPoint;
import com.example.demo.security.jwt.OncePerRequestFilterR5A05;
import com.example.demo.security.jwt.UserDetailsServiceR5A05;

@Configuration
@EnableMethodSecurity // Active les annotations comme @PreAuthorize
public class R5A05WebSecurityConfigurer {

    @Autowired
    private UserDetailsServiceR5A05 userDetailsService;

    @Autowired
    private JwtAuthEntryPoint unauthorizedHandler;

    // Définit un filtre personnalisé pour valider les JWT
    @Bean
    public OncePerRequestFilterR5A05 authenticationJwtTokenFilter() {
        return new OncePerRequestFilterR5A05();
    }

    // Expose l'AuthenticationManager en utilisant AuthenticationConfiguration
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // Fournit un encodeur de mot de passe (non sécurisé, uniquement pour les tests)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    // Configuration de la sécurité HTTP
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable() // Désactive CSRF pour les APIs REST
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and() // Gestion des erreurs
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and() // Pas de sessions côté serveur
                .authorizeHttpRequests()
                .requestMatchers("/login").permitAll() // Autorise l'accès à /login
                .anyRequest().authenticated(); // Toutes les autres requêtes nécessitent une authentification

        // Ajoute le filtre JWT avant le filtre standard d'authentification
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
