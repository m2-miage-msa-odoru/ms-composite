package fello.miage.endpoints;

import fello.miage.requests.CreateCompetitionRequest;
import fello.miage.requests.NoterCompetitionRequest;
import fello.miage.responses.CompetitionDTO;
import fello.miage.responses.ResultatDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/composite/competitions")
public interface CompositeCompetitionEndpoint {

    @Operation(description = "Crée une compétition après vérification que l'utilisateur est ENSEIGNANT, apte au niveau, et que la date est au moins J+7")
    @ApiResponse(responseCode = "201", description = "Compétition créée")
    @ApiResponse(responseCode = "403", description = "L'utilisateur n'a pas le rôle ENSEIGNANT ou n'est pas apte au niveau")
    @ApiResponse(responseCode = "404", description = "Utilisateur introuvable")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping("/")
    CompetitionDTO createCompetition(@RequestBody CreateCompetitionRequest request);

    @Operation(description = "Liste les compétitions organisées par un enseignant (par email)")
    @ApiResponse(responseCode = "200", description = "Liste retournée")
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("/by-enseignant/{email}")
    List<CompetitionDTO> getCompetitionsByEnseignant(@PathVariable("email") String email);

    @Operation(description = "Liste les compétitions correspondant au niveau d'expertise d'un utilisateur (par email)")
    @ApiResponse(responseCode = "200", description = "Liste retournée")
    @ApiResponse(responseCode = "404", description = "Utilisateur introuvable")
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("/by-eleve/{email}")
    List<CompetitionDTO> getCompetitionsByEleve(@PathVariable("email") String email);

    @Operation(description = "Attribue une note (sur 10) à un adhérent pour une compétition. "
            + "Vérifie que l'enseignant a bien le rôle ENSEIGNANT et un niveau supérieur ou égal au niveau de la compétition, "
            + "et que l'adhérent a exactement le niveau de la compétition.")
    @ApiResponse(responseCode = "201", description = "Note enregistrée")
    @ApiResponse(responseCode = "403", description = "L'enseignant n'a pas le rôle requis / niveau insuffisant, ou l'adhérent n'a pas le niveau exact de la compétition")
    @ApiResponse(responseCode = "404", description = "Adhérent, enseignant ou compétition introuvable")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping("/noter")
    ResultatDTO noterMembre(@RequestBody NoterCompetitionRequest request);
}
