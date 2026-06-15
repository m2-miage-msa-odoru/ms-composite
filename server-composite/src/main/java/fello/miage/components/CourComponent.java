package fello.miage.components;

import fello.miage.modeles.CourEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Client Feign vers ms-cours.
 */
@FeignClient(name = "cours-service")
public interface CourComponent {

    @PostMapping("/api/v1/cours/create")
    List<CourEntity> createCours(@RequestBody List<CourEntity> cours);

    @GetMapping("/api/v1/cours/by-enseignant/{email}")
    List<CourEntity> getCoursByEnseignantEmail(@PathVariable("email") String email);

    @GetMapping("/api/v1/cours/by-niveau/{niveau}")
    List<CourEntity> getCoursByNiveau(@PathVariable("niveau") String niveau);

    @GetMapping("/api/v1/cours/{id}")
    CourEntity getCoursById(@PathVariable("id") String id);
}
