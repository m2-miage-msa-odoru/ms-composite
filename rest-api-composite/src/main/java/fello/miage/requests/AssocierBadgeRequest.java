package fello.miage.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssocierBadgeRequest {

    @Schema(description = "Email de la secrétaire qui réalise l'opération (vérification du rôle SECRETAIRE)")
    @Email
    @NotBlank
    private String secretaire_email;

    @Schema(description = "Numéro du badge à associer")
    @NotNull
    private Integer numero;

    @Schema(description = "Email de l'adhérent qui devient propriétaire du badge")
    @Email
    @NotBlank
    private String adherent_email;
}
