package fello.miage.services;

import fello.miage.components.StatsComponent;
import fello.miage.components.UserComponent;
import fello.miage.enums.RoleMembre;
import fello.miage.exceptions.rest.ForbiddenRestException;
import fello.miage.exceptions.technical.UserNotFoundException;
import fello.miage.modeles.UserEntity;
import fello.miage.responses.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Orchestre les statistiques : vérifie le rôle PRESIDENT (ms-user) avant de relayer vers ms-stats.
 */
@Service
@AllArgsConstructor
@Slf4j
public class CompositeStatsService {

    private final UserComponent userComponent;
    private final StatsComponent statsComponent;

    /** Vérifie que l'utilisateur existe et possède le rôle PRESIDENT. */
    private void verifierPresident(String email) {
        UserEntity user;
        try {
            user = userComponent.getMembreByEmail(email);
        } catch (Exception e) {
            log.warn("Utilisateur introuvable : {}", email);
            throw new UserNotFoundException("Utilisateur introuvable : " + email);
        }
        if (user.getRole() != RoleMembre.PRESIDENT) {
            throw new ForbiddenRestException(
                    "Seul un PRESIDENT peut consulter les statistiques. Rôle actuel : " + user.getRole());
        }
    }

    public CoursPresenceMoyenneDTO coursEtPresenceMoyenne(String presidentEmail) {
        verifierPresident(presidentEmail);
        return statsComponent.coursEtPresenceMoyenne();
    }

    public ElevesPresentsCoursDTO elevesPresentsParCours(String idCours, String presidentEmail) {
        verifierPresident(presidentEmail);
        return statsComponent.elevesPresentsParCours(idCours);
    }

    public List<CoursEleveDTO> coursDUnEleve(String email, String presidentEmail, String debut, String fin) {
        verifierPresident(presidentEmail);
        return statsComponent.coursDUnEleve(email, debut, fin);
    }

    public CompetitionCountNiveauDTO nombreCompetitionsParNiveau(String niveau, String presidentEmail) {
        verifierPresident(presidentEmail);
        return statsComponent.nombreCompetitionsParNiveau(niveau);
    }

    public List<CompetitionEleveDTO> competitionsDUnEleve(String email, String presidentEmail, String debut, String fin) {
        verifierPresident(presidentEmail);
        return statsComponent.competitionsDUnEleve(email, debut, fin);
    }
}
