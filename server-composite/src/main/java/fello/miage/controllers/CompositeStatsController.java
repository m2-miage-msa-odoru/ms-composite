package fello.miage.controllers;

import fello.miage.endpoints.CompositeStatsEndpoint;
import fello.miage.responses.*;
import fello.miage.services.CompositeStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CompositeStatsController implements CompositeStatsEndpoint {

    private final CompositeStatsService compositeStatsService;

    @Override
    public CoursPresenceMoyenneDTO coursEtPresenceMoyenne(String presidentEmail) {
        return compositeStatsService.coursEtPresenceMoyenne(presidentEmail);
    }

    @Override
    public ElevesPresentsCoursDTO elevesPresentsParCours(String idCours, String presidentEmail) {
        return compositeStatsService.elevesPresentsParCours(idCours, presidentEmail);
    }

    @Override
    public List<CoursEleveDTO> coursDUnEleve(String email, String presidentEmail, String debut, String fin) {
        return compositeStatsService.coursDUnEleve(email, presidentEmail, debut, fin);
    }

    @Override
    public CompetitionCountNiveauDTO nombreCompetitionsParNiveau(String niveau, String presidentEmail) {
        return compositeStatsService.nombreCompetitionsParNiveau(niveau, presidentEmail);
    }

    @Override
    public List<CompetitionEleveDTO> competitionsDUnEleve(String email, String presidentEmail, String debut, String fin) {
        return compositeStatsService.competitionsDUnEleve(email, presidentEmail, debut, fin);
    }
}
