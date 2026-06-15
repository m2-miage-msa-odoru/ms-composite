package fello.miage.endpoints;

import fello.miage.requests.AssocierBadgeRequest;
import fello.miage.requests.BadgerRequest;
import fello.miage.requests.CreateBadgeRequest;
import fello.miage.responses.BadgeDTO;
import fello.miage.responses.BadgeageDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/composite/badges")
public interface CompositeBadgeEndpoint {

    @Operation(description = "Enregistre un badge. Vérifie que l'utilisateur a le rôle SECRETAIRE (appel ms-user), "
            + "puis enregistre le numéro via ms-badge avec un propriétaire (email_utilisateur) null.")
    @ApiResponse(responseCode = "201", description = "Badge enregistré")
    @ApiResponse(responseCode = "403", description = "L'utilisateur n'a pas le rôle SECRETAIRE")
    @ApiResponse(responseCode = "404", description = "Secrétaire introuvable")
    @ApiResponse(responseCode = "409", description = "Un badge avec ce numéro existe déjà")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping("/")
    BadgeDTO enregistrerBadge(@RequestBody CreateBadgeRequest request);

    @Operation(description = "Associe un adhérent comme propriétaire d'un badge. Vérifie que l'opérateur a le rôle "
            + "SECRETAIRE et que l'adhérent existe (appels ms-user), puis met à jour le propriétaire via ms-badge.")
    @ApiResponse(responseCode = "200", description = "Propriétaire associé")
    @ApiResponse(responseCode = "403", description = "L'opérateur n'a pas le rôle SECRETAIRE")
    @ApiResponse(responseCode = "404", description = "Secrétaire ou adhérent introuvable, ou badge introuvable")
    @ResponseStatus(code = HttpStatus.OK)
    @PutMapping("/proprietaire")
    BadgeDTO associerProprietaire(@RequestBody AssocierBadgeRequest request);

    @Operation(description = "Simule l'action de badger d'un adhérent à un cours. Vérifie que le badge existe et a un "
            + "propriétaire (ms-badge), que le cours existe (ms-cours), et que le niveau du cours est identique au "
            + "niveau d'expertise du propriétaire (ms-user), puis enregistre le badgeage.")
    @ApiResponse(responseCode = "201", description = "Badgeage enregistré")
    @ApiResponse(responseCode = "400", description = "Badge sans propriétaire ou niveau du cours différent du niveau de l'adhérent")
    @ApiResponse(responseCode = "404", description = "Badge, cours ou propriétaire introuvable")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping("/badger")
    BadgeageDTO badger(@RequestBody BadgerRequest request);
}
