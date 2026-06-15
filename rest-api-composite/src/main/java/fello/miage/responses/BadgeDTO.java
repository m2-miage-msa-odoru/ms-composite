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
public class BadgeDTO {

    @Schema(description = "Identifiant technique du badge")
    private Long id;

    @Schema(description = "Numéro du badge (unique)")
    private Integer numero;

    @Schema(description = "Email de l'utilisateur propriétaire (null tant qu'il n'est pas attribué)")
    private String email_utilisateur;
}
