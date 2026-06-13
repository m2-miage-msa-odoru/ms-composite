package fello.miage.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCompetitionRequest {

    @Schema(description = "Email de l'enseignant qui crée la compétition")
    @Email
    @NotBlank
    private String email;

    @Schema(description = "Titre de la compétition")
    @NotBlank
    private String titre;

    @Schema(description = "Niveau cible de la compétition")
    @NotBlank
    private String niveau_cible;

    @Schema(description = "Date de début de la compétition")
    private LocalDateTime debut;

    @Schema(description = "Durée de la compétition en minutes")
    @NotNull
    private Integer duree;

    @Schema(description = "Lieu de la compétition")
    private String lieu;
}
