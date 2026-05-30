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
public class CreateCourRequest {

    @Schema(description = "Email de l'enseignant qui crée le cours")
    @Email
    @NotBlank
    private String email;

    @Schema(description = "Titre du cours")
    @NotBlank
    private String titre;

    @Schema(description = "Niveau cible du cours")
    @NotBlank
    private String niveau_cible;

    @Schema(description = "Date de début du cours")
    private LocalDateTime debut;

    @Schema(description = "Durée du cours en minutes")
    @NotNull
    private Integer duree;

    @Schema(description = "Lieu du cours")
    private String lieu;
}
