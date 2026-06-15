package fello.miage.controllers;

import fello.miage.endpoints.CompositeBadgeEndpoint;
import fello.miage.requests.AssocierBadgeRequest;
import fello.miage.requests.BadgerRequest;
import fello.miage.requests.CreateBadgeRequest;
import fello.miage.responses.BadgeDTO;
import fello.miage.responses.BadgeageDTO;
import fello.miage.services.CompositeBadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CompositeBadgeController implements CompositeBadgeEndpoint {

    private final CompositeBadgeService compositeBadgeService;

    @Override
    public BadgeDTO enregistrerBadge(CreateBadgeRequest request) {
        return compositeBadgeService.enregistrerBadge(request);
    }

    @Override
    public BadgeDTO associerProprietaire(AssocierBadgeRequest request) {
        return compositeBadgeService.associerProprietaire(request);
    }

    @Override
    public BadgeageDTO badger(BadgerRequest request) {
        return compositeBadgeService.badger(request);
    }
}
