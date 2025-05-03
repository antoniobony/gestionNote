package com.example.gestiondigitalisation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class EtudiantDto {
    @Valid

    @NotNull
    @NotEmpty
    @Pattern(regexp = "^[A-Za-zÀ-ÿ]+(?:\\s[A-Za-zÀ-ÿ]+)*$", message = "Seules les lettres sont autorisées")
    private String nom;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "^(?:[0-9]|[1][0-9]|[2][0])(?:\\.[0-9]{1,2})?$", message = "La moyenne doit être un nombre entre 0 et 20 avec jusqu'à 2 décimales")
    private String moyenne;
}
