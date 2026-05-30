package fello.miage.modeles;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Représentation locale d'un cours.
 * Mêmes noms de champs que CourEntity de ms-cours pour cohérence
 * (sert aussi de payload JSON envoyé à ms-cours).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CourEntity {
    private String id_cour;
    private String titre;
    private String niveau_cible;
    private LocalDateTime debut;
    private Integer duree;
    private String lieu;
    private String enseignant_email;
}
