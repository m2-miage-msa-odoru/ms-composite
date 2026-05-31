package fello.miage.controllers;

import fello.miage.endpoints.CompositeCourEndpoint;
import fello.miage.requests.CreateCourRequest;
import fello.miage.responses.CourDTO;
import fello.miage.services.CompositeCourService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CompositeCourController implements CompositeCourEndpoint {

    private final CompositeCourService compositeCourService;

    @Override
    public CourDTO createCour(CreateCourRequest request) {
        return compositeCourService.createCour(request);
    }

    @Override
    public List<CourDTO> getCoursByEnseignant(String email) {
        return compositeCourService.getCoursByEnseignant(email);
    }

    @Override
    public List<CourDTO> getCoursByEleve(String email) {
        return compositeCourService.getCoursByEleve(email);
    }
}
