package fello.miage.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BadgeageDTO {

    @Schema(description = "Identifiant du badgeage")
    private Long id;

    @Schema(description = "Numéro du badge utilisé")
    private Integer numero_badge;

    @Schema(description = "Identifiant du cours suivi")
    private String id_cours;

    @Schema(description = "Email du propriétaire du badge")
    private String email_utilisateur;
}
