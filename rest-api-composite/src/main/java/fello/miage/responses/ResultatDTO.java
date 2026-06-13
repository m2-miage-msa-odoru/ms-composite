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
public class ResultatDTO {

    @Schema(description = "Identifiant du résultat")
    private String id_resultat;

    @Schema(description = "Identifiant de la compétition concernée")
    private String id_competition;

    @Schema(description = "Email de l'adhérent noté")
    private String adherent_email;

    @Schema(description = "Note sur 10 (entier de 0 à 10)")
    private Integer note;
}
