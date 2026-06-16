package fello.miage.endpoints;

import fello.miage.responses.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Statistiques du club — accessibles uniquement au PRESIDENT.
 * Le rôle est vérifié ici (via ms-user) avant de relayer vers ms-stats.
 */
@RestController
@RequestMapping("api/v1/composite/stats")
public interface CompositeStatsEndpoint {

    @Operation(description = "Nombre de cours et nombre moyen d'élèves présents (PRESIDENT requis)")
    @ApiResponse(responseCode = "200", description = "Statistique calculée")
    @ApiResponse(responseCode = "403", description = "L'utilisateur n'a pas le rôle PRESIDENT")
    @ApiResponse(responseCode = "404", description = "Utilisateur introuvable")
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("/cours/presence-moyenne")
    CoursPresenceMoyenneDTO coursEtPresenceMoyenne(@RequestParam("president_email") String presidentEmail);

    @Operation(description = "Nombre et liste des élèves présents à un cours donné (PRESIDENT requis)")
    @ApiResponse(responseCode = "200", description = "Statistique calculée")
    @ApiResponse(responseCode = "403", description = "L'utilisateur n'a pas le rôle PRESIDENT")
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("/cours/{idCours}/presents")
    ElevesPresentsCoursDTO elevesPresentsParCours(@PathVariable("idCours") String idCours,
                                                  @RequestParam("president_email") String presidentEmail);

    @Operation(description = "Liste des cours d'un élève (filtrable par période) avec présence/absence (PRESIDENT requis)")
    @ApiResponse(responseCode = "200", description = "Liste retournée")
    @ApiResponse(responseCode = "403", description = "L'utilisateur n'a pas le rôle PRESIDENT")
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("/eleve/{email}/cours")
    List<CoursEleveDTO> coursDUnEleve(@PathVariable("email") String email,
                                      @RequestParam("president_email") String presidentEmail,
                                      @RequestParam(value = "debut", required = false) String debut,
                                      @RequestParam(value = "fin", required = false) String fin);

    @Operation(description = "Nombre de compétitions pour un niveau donné (PRESIDENT requis)")
    @ApiResponse(responseCode = "200", description = "Statistique calculée")
    @ApiResponse(responseCode = "403", description = "L'utilisateur n'a pas le rôle PRESIDENT")
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("/competitions/count-by-niveau/{niveau}")
    CompetitionCountNiveauDTO nombreCompetitionsParNiveau(@PathVariable("niveau") String niveau,
                                                          @RequestParam("president_email") String presidentEmail);

    @Operation(description = "Liste des compétitions d'un élève (filtrable par période) avec résultats (PRESIDENT requis)")
    @ApiResponse(responseCode = "200", description = "Liste retournée")
    @ApiResponse(responseCode = "403", description = "L'utilisateur n'a pas le rôle PRESIDENT")
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("/eleve/{email}/competitions")
    List<CompetitionEleveDTO> competitionsDUnEleve(@PathVariable("email") String email,
                                                   @RequestParam("president_email") String presidentEmail,
                                                   @RequestParam(value = "debut", required = false) String debut,
                                                   @RequestParam(value = "fin", required = false) String fin);
}
