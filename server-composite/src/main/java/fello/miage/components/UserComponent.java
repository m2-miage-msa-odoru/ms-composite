package fello.miage.components;

import fello.miage.modeles.UserEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Client Feign vers ms-user.
 * Le name doit correspondre au spring.application.name enregistré dans Eureka.
 */
@FeignClient(name = "user-service")
public interface UserComponent {

    @GetMapping("/api/v1/membres/{email}")
    UserEntity getMembreByEmail(@PathVariable("email") String email);
}
