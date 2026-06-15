package fello.miage.modeles;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Représentation locale d'un badgeage.
 * Mêmes noms de champs que BadgeageEntity de ms-badge
 * (sert aussi de payload JSON échangé avec ms-badge).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BadgeageEntity {
    private Long id;
    private Integer numero_badge;
    private String id_cours;
    private String email_utilisateur;
}
