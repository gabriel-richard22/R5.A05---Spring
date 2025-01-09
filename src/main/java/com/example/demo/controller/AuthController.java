package com.example.demo.controller;

import com.example.demo.security.TokenGenerator;
import com.example.demo.security.jwt.JwtDTO;
import com.example.demo.entity.UtilisateurEntity;
import com.example.demo.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private UtilisateurRepository utilisateurRepository;  // Pour récupérer les utilisateurs si nécessaire

    // Endpoint pour l'authentification
    @PostMapping("/login")
    public JwtDTO authenticateUser(@RequestBody AuthRequest authRequest) {
        // Authentifier l'utilisateur avec ses identifiants (email et mot de passe)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );

        // Générer le JWT après authentification
        String jwt = tokenGenerator.generateJwtToken(authentication);

        // Retourner le JWT dans un format DTO
        return new JwtDTO(jwt, "Bearer", authentication.getName(), authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList()));
    }

    // Exemple d'endpoint protégé accessible seulement par les administrateurs
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/adminPage")
    public String adminPage() {
        return "Page réservée aux administrateurs";
    }

    // Exemple d'endpoint protégé accessible seulement par les utilisateurs
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/userPage")
    public String userPage() {
        return "Page réservée aux utilisateurs";
    }

    // Endpoint pour récupérer tous les utilisateurs, accessible aux administrateurs uniquement
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/utilisateurs")
    public List<UtilisateurEntity> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    // Classe interne pour la demande de connexion
    public static class AuthRequest {
        private String email;
        private String password;

        // Getters et Setters
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
