package com.example.demo.security.jwt;

import com.example.demo.entity.UtilisateurEntity;
import com.example.demo.repository.UtilisateurRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

@Service // Indique que cette classe est un service Spring
public class UserDetailsServiceR5A05 implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;

    // Injection du repository pour récupérer les utilisateurs de la base de données
    public UserDetailsServiceR5A05(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Recherche l'utilisateur par son email (on suppose que le username est l'email)
        UtilisateurEntity utilisateur = utilisateurRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        // Assurez-vous que les rôles ne sont pas null pour éviter les exceptions
        List<SimpleGrantedAuthority> authorities = utilisateur.getRoles() != null
                ? utilisateur.getRoles().stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList())
                : List.of();

        // Retourner un objet User de Spring Security, avec email, mot de passe et rôles
        return new User(utilisateur.getEmail(), utilisateur.getPassword(), authorities);
    }
}
