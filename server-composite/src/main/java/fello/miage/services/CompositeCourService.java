package fello.miage.services;

import fello.miage.components.CourComponent;
import fello.miage.components.UserComponent;
import fello.miage.enums.RoleMembre;
import fello.miage.exceptions.rest.ForbiddenRestException;
import fello.miage.exceptions.technical.UserNotFoundException;
import fello.miage.mappers.CompositeCourMapper;
import fello.miage.modeles.CourEntity;
import fello.miage.modeles.UserEntity;
import fello.miage.requests.CreateCourRequest;
import fello.miage.responses.CourDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CompositeCourService {

    private final UserComponent userComponent;
    private final CourComponent courComponent;
    private final CompositeCourMapper mapper;

    public CourDTO createCour(CreateCourRequest request) {
        // 1. Récupère l'utilisateur depuis ms-user
        UserEntity user;
        try {
            user = userComponent.getMembreByEmail(request.getEmail());
        } catch (Exception e) {
            log.warn("Utilisateur introuvable : {}", request.getEmail());
            throw new UserNotFoundException("Utilisateur introuvable : " + request.getEmail());
        }

        // 2. Vérifie le rôle ENSEIGNANT
        if (user.getRole() != RoleMembre.ENSEIGNANT) {
            throw new ForbiddenRestException(
                    "Seul un ENSEIGNANT peut créer un cours. Rôle actuel : " + user.getRole());
        }

        // 3. Envoie la création à ms-cours (en liste de 1 car le endpoint est batch)
        CourEntity toSave = mapper.toEntity(request);
        List<CourEntity> saved = courComponent.createCours(List.of(toSave));

        // 4. Retourne le dernier cours créé
        CourEntity lastCreated = saved.get(saved.size() - 1);
        return mapper.toDTO(lastCreated);
    }
}
