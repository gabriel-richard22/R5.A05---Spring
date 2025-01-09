package com.example.demo.controller;

import com.example.demo.entity.Utilisateur;
import com.example.demo.repository.UtilisateurRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/utilisateurs")  // Point d'entrée pour les utilisateurs
public class UtilisateurController {

    @Autowired
    private UtilisateurRepository repository;  // Utilisation de votre repository nommé Repository

    // Endpoint pour récupérer tous les utilisateurs
    @GetMapping
    public List<Utilisateur> getAllUtilisateurs() {
        return repository.findAll();
    }

    // Endpoint pour créer un nouvel utilisateur
    @PostMapping
    public Utilisateur createUtilisateur(@RequestBody Utilisateur utilisateur) {
        return repository.save(utilisateur);
    }

    // Endpoint pour récupérer un utilisateur par son ID
    @GetMapping("/{id}")
    public Utilisateur getUtilisateurById(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }
}
