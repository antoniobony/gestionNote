package com.example.gestiondigitalisation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface EtudiantService {
    List<Etudiant> findAll();

    Page<Etudiant> findAllPage(Pageable pageable);

    Etudiant findById(Long id);

    Page<Etudiant> findByNom(Pageable pageable, String nom);

    Page<Etudiant> findByMoyenne(Pageable pageable, String moyenne);

    Etudiant create(EtudiantDto etudiant);

    Etudiant updateEtudiant(EtudiantDto etudiant,Long id);

    Map<String,String> deleteEtudiantById(Long id);
}
