package fello.miage.controllers;

import fello.miage.endpoints.CompositeCourEndpoint;
import fello.miage.requests.CreateCourRequest;
import fello.miage.responses.CourDTO;
import fello.miage.services.CompositeCourService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CompositeCourController implements CompositeCourEndpoint {

    private final CompositeCourService compositeCourService;

    @Override
    public CourDTO createCour(CreateCourRequest request) {
        return compositeCourService.createCour(request);
    }
}
