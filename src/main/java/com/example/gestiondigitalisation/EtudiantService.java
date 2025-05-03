package com.example.gestiondigitalisation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EtudiantService {
    private final EtudiantRepository etudiantRepository;

    public List<Etudiant> findAll() {
        return etudiantRepository.findAll();
    }

    public Page<Etudiant> findAllPage(Pageable pageable){
        return etudiantRepository.findAll(pageable);
    }

    public Etudiant findById(Long id) {
        return etudiantRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Le numEt " + id + " n'existe pas "));
    }

    public Page<Etudiant> findByNom(Pageable pageable, String nom){
        if(etudiantRepository.findByNom(nom,pageable).isEmpty())
            throw new RuntimeException("Nom n'existe pas");
        return etudiantRepository.findByNom(nom,pageable);
    }

    public Page<Etudiant> findByMoyenne(Pageable pageable, String moyenne){
        if(etudiantRepository.findByMoyenne(moyenne,pageable).isEmpty())
            throw new RuntimeException("La moyenne n'existe pas");
        return etudiantRepository.findByNom(moyenne,pageable);
    }

    public Etudiant create(Etudiant etudiant) {
        if(etudiantRepository.findByNom(etudiant.getNom()).isEmpty()){
            return etudiantRepository.save(etudiant);
        }
        throw new RuntimeException("Nom existe déjà");
    }

    public Etudiant updateEtudiant(Etudiant etudiant,Long id) {
        if(etudiantRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Le numEt " + id + " n'existe pas ");
        } else if (etudiantRepository.findByNomAndNumEtIsNot(etudiant.getNom(),etudiant.getNumEt()).isPresent()) {
            throw new IllegalArgumentException("Le nom " + etudiant.getNom() + " existe déjà ");
        }
        return etudiantRepository.save(etudiant);
    }

    public Map<String,String> deleteEtudiantById(Long id) {
        if(etudiantRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Le numEt " + id + " n'existe pas ");
        }
        etudiantRepository.deleteById(id);
        Map<String,String> message = new HashMap<>();
        message.put("message","Etudiant deleted successfully");

        return message;
    }
}
