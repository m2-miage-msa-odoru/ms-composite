package fello.miage.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionCountNiveauDTO {
    private String niveau;
    private Long nombreCompetitions;
}
