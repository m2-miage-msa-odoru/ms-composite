package fello.miage.controllers;

import fello.miage.endpoints.CompositeCompetitionEndpoint;
import fello.miage.requests.CreateCompetitionRequest;
import fello.miage.requests.NoterCompetitionRequest;
import fello.miage.responses.CompetitionDTO;
import fello.miage.responses.ResultatDTO;
import fello.miage.services.CompositeCompetitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CompositeCompetitionController implements CompositeCompetitionEndpoint {

    private final CompositeCompetitionService compositeCompetitionService;

    @Override
    public CompetitionDTO createCompetition(CreateCompetitionRequest request) {
        return compositeCompetitionService.createCompetition(request);
    }

    @Override
    public List<CompetitionDTO> getCompetitionsByEnseignant(String email) {
        return compositeCompetitionService.getCompetitionsByEnseignant(email);
    }

    @Override
    public List<CompetitionDTO> getCompetitionsByEleve(String email) {
        return compositeCompetitionService.getCompetitionsByEleve(email);
    }

    @Override
    public ResultatDTO noterMembre(NoterCompetitionRequest request) {
        return compositeCompetitionService.noterMembre(request);
    }
}
