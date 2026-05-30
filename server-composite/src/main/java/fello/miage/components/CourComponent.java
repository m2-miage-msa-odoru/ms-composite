package fello.miage.components;

import fello.miage.modeles.CourEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Client Feign vers ms-cours.
 * Le endpoint POST /api/v1/cours/create attend une liste de cours.
 */
@FeignClient(name = "cours-service")
public interface CourComponent {

    @PostMapping("/api/v1/cours/create")
    List<CourEntity> createCours(@RequestBody List<CourEntity> cours);
}
