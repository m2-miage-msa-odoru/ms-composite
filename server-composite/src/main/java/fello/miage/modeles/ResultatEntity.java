package fello.miage.modeles;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Représentation locale d'un résultat (note) de compétition.
 * Mêmes noms de champs que ResultatEntity de ms-competition pour cohérence
 * (sert aussi de payload JSON envoyé à ms-competition).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultatEntity {
    private String id_resultat;
    private String id_competition;
    private String adherent_email;
    private Integer note;
}
