package fello.miage.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElevesPresentsCoursDTO {
    private String id_cours;
    private Long nombrePresents;
    private List<String> eleves;
}
