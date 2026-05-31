package fello.miage.endpoints;

import fello.miage.requests.CreateCourRequest;
import fello.miage.responses.CourDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/composite/cours")
public interface CompositeCourEndpoint {

    @Operation(description = "Crée un cours après vérification que l'utilisateur est ENSEIGNANT")
    @ApiResponse(responseCode = "201", description = "Cours créé")
    @ApiResponse(responseCode = "403", description = "L'utilisateur n'a pas le rôle ENSEIGNANT")
    @ApiResponse(responseCode = "404", description = "Utilisateur introuvable")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping("/")
    CourDTO createCour(@RequestBody CreateCourRequest request);

    @Operation(description = "Liste les cours donnés par un enseignant (par email)")
    @ApiResponse(responseCode = "200", description = "Liste retournée")
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("/by-enseignant/{email}")
    List<CourDTO> getCoursByEnseignant(@PathVariable("email") String email);

    @Operation(description = "Liste les cours correspondant au niveau d'expertise d'un utilisateur (par email)")
    @ApiResponse(responseCode = "200", description = "Liste retournée")
    @ApiResponse(responseCode = "404", description = "Utilisateur introuvable")
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("/by-eleve/{email}")
    List<CourDTO> getCoursByEleve(@PathVariable("email") String email);
}
