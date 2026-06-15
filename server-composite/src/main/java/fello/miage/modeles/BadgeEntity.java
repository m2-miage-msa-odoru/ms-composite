package fello.miage.modeles;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Représentation locale d'un badge.
 * Mêmes noms de champs que BadgeEntity de ms-badge pour cohérence
 * (sert aussi de payload JSON échangé avec ms-badge).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BadgeEntity {
    private Long id;
    private Integer numero;
    private String email_utilisateur;
}
