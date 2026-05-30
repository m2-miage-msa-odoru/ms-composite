package fello.miage.modeles;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fello.miage.enums.RoleMembre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Représentation locale d'un utilisateur récupéré depuis ms-user.
 * Mêmes noms de champs que UserEntity de ms-user pour cohérence.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserEntity {
    private String email;
    private String prenom;
    private String nom;
    private RoleMembre role;
    private Integer niveau_expertise;
}
