package fello.miage.components;

import fello.miage.modeles.CompetitionEntity;
import fello.miage.modeles.ResultatEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Client Feign vers ms-competition.
 */
@FeignClient(name = "competition-service")
public interface CompetitionComponent {

    @PostMapping("/api/v1/competitions/create")
    List<CompetitionEntity> createCompetitions(@RequestBody List<CompetitionEntity> competitions);

    @GetMapping("/api/v1/competitions/by-enseignant/{email}")
    List<CompetitionEntity> getCompetitionsByEnseignantEmail(@PathVariable("email") String email);

    @GetMapping("/api/v1/competitions/by-niveau/{niveau}")
    List<CompetitionEntity> getCompetitionsByNiveau(@PathVariable("niveau") String niveau);

    @GetMapping("/api/v1/competitions/{id}")
    CompetitionEntity getCompetitionById(@PathVariable("id") String id);

    @PostMapping("/api/v1/competitions/resultats")
    ResultatEntity enregistrerResultat(@RequestBody ResultatEntity resultat);
}
