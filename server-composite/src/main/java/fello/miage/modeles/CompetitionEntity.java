package fello.miage.modeles;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Représentation locale d'une compétition.
 * Mêmes noms de champs que CompetitionEntity de ms-competition pour cohérence
 * (sert aussi de payload JSON envoyé à ms-competition).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompetitionEntity {
    private String id_competition;
    private String titre;
    private String niveau_cible;
    private LocalDateTime debut;
    private Integer duree;
    private String lieu;
    private String enseignant_email;
}
