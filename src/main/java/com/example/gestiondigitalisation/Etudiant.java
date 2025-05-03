package com.example.gestiondigitalisation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Etudiants")
public class Etudiant {
    @Id
    @SequenceGenerator(
            name = "Etudiant_sequence",
            sequenceName = "Etudiant_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "Etudiant_sequence"
    )
    private Long numEt;

    @Column( nullable = false,unique = true)
    private String nom;

    @Column(nullable = false)
    private String moyenne;

}
