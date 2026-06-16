package fello.miage.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionEleveDTO {
    private String id_competition;
    private String titre;
    private String niveau_cible;
    private LocalDateTime debut;
    private Integer note;
}
