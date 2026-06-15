package fello.miage.components;

import fello.miage.modeles.BadgeEntity;
import fello.miage.modeles.BadgeageEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Client Feign vers ms-badge.
 * Le name doit correspondre au spring.application.name enregistré dans Eureka.
 */
@FeignClient(name = "badge-service")
public interface BadgeComponent {

    @PostMapping("/api/v1/badges/")
    BadgeEntity enregistrerBadge(@RequestBody BadgeEntity badge);

    @PutMapping("/api/v1/badges/proprietaire")
    BadgeEntity associerProprietaire(@RequestBody BadgeEntity badge);

    @GetMapping("/api/v1/badges/{numero}")
    BadgeEntity getBadgeByNumero(@PathVariable("numero") Integer numero);

    @PostMapping("/api/v1/badges/badgeages")
    BadgeageEntity enregistrerBadgeage(@RequestBody BadgeageEntity badgeage);
}
