package com.example.demo.repository;

import com.example.demo.entity.UtilisateurEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<UtilisateurEntity, Long> {
    // Recherche un utilisateur par son email
    Optional<UtilisateurEntity> findByEmail(String email);
}
