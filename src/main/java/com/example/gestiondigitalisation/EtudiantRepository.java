package com.example.gestiondigitalisation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {

    Page<Etudiant> findByNom(String nom, Pageable pageable);
    Page<Etudiant> findByMoyenne(String moyenne, Pageable pageable);
    Optional<Etudiant> findByNomAndNumEtIsNot(String nom, Long numEmp);
    Optional<Etudiant> findByNom(String nom);
}
