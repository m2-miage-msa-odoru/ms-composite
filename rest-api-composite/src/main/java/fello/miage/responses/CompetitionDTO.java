package fello.miage.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionDTO {

    @Schema(description = "Identifiant MongoDB de la compétition")
    private String id_competition;

    private String titre;
    private String niveau_cible;
    private LocalDateTime debut;
    private Integer duree;
    private String lieu;
    private String enseignant_email;
}
