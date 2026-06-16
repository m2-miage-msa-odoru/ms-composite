package fello.miage.components;

import fello.miage.responses.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Client Feign vers ms-stats.
 */
@FeignClient(name = "stats-service")
public interface StatsComponent {

    @GetMapping("/api/v1/stats/cours/presence-moyenne")
    CoursPresenceMoyenneDTO coursEtPresenceMoyenne();

    @GetMapping("/api/v1/stats/cours/{idCours}/presents")
    ElevesPresentsCoursDTO elevesPresentsParCours(@PathVariable("idCours") String idCours);

    @GetMapping("/api/v1/stats/eleve/{email}/cours")
    List<CoursEleveDTO> coursDUnEleve(@PathVariable("email") String email,
                                      @RequestParam(value = "debut", required = false) String debut,
                                      @RequestParam(value = "fin", required = false) String fin);

    @GetMapping("/api/v1/stats/competitions/count-by-niveau/{niveau}")
    CompetitionCountNiveauDTO nombreCompetitionsParNiveau(@PathVariable("niveau") String niveau);

    @GetMapping("/api/v1/stats/eleve/{email}/competitions")
    List<CompetitionEleveDTO> competitionsDUnEleve(@PathVariable("email") String email,
                                                   @RequestParam(value = "debut", required = false) String debut,
                                                   @RequestParam(value = "fin", required = false) String fin);
}
