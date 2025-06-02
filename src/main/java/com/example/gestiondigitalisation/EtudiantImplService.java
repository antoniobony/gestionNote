package com.example.gestiondigitalisation;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
@RequiredArgsConstructor
public class EtudiantImplService implements EtudiantService {

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

    public Etudiant create(EtudiantDto etudiant) {
        Etudiant etudiant1  = Etudiant.builder()
                .moyenne(etudiant.getMoyenne())
                .nom(etudiant.getNom())
                .build();
        if(etudiantRepository.findByNom(etudiant1.getNom()).isEmpty()){
            return etudiantRepository.save(etudiant1);
        }
        throw new RuntimeException("Nom existe déjà");
    }

    public Etudiant updateEtudiant(EtudiantDto etudiant,Long id) {
        Etudiant etudiant1 = Etudiant.builder().
                nom(etudiant.getNom()).
                moyenne(etudiant.getMoyenne()).build();

        if(etudiantRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Le numEt " + id + " n'existe pas ");
        } else if (etudiantRepository.findByNomAndNumEtIsNot(etudiant1.getNom(),etudiant1.getNumEt()).isPresent()) {
            throw new IllegalArgumentException("Le nom " + etudiant1.getNom() + " existe déjà ");
        }
        return etudiantRepository.save(etudiant1);
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
