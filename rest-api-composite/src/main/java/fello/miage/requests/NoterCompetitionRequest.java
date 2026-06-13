package fello.miage.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoterCompetitionRequest {

    @Schema(description = "Email de l'adhérent à noter")
    @Email
    @NotBlank
    private String adherent_email;

    @Schema(description = "Email de l'enseignant qui saisit la note (sert uniquement à la vérification du rôle et du niveau)")
    @Email
    @NotBlank
    private String enseignant_email;

    @Schema(description = "Note sur 10 (entier de 0 à 10)")
    @NotNull
    @Min(0)
    @Max(10)
    private Integer note;

    @Schema(description = "Identifiant de la compétition concernée")
    @NotBlank
    private String id_competition;
}
