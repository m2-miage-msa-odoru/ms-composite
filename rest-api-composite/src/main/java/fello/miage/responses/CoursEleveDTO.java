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
public class CoursEleveDTO {
    private String id_cour;
    private String titre;
    private String niveau_cible;
    private LocalDateTime debut;
    private boolean present;
}
