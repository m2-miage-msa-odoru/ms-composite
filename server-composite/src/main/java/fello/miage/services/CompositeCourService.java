package fello.miage.services;

import fello.miage.components.CourComponent;
import fello.miage.components.UserComponent;
import fello.miage.enums.RoleMembre;
import fello.miage.exceptions.rest.BadRequestRestException;
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

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CompositeCourService {

    private final UserComponent userComponent;
    private final CourComponent courComponent;
    private final CompositeCourMapper mapper;

    public CourDTO createCour(CreateCourRequest request) {
        // 0. Règle métier : la date du cours doit être au moins J+7 (jours calendaires)
        if (request.getDebut() == null) {
            throw new BadRequestRestException("La date de début du cours est obligatoire");
        }
        LocalDate dateCour = request.getDebut().toLocalDate();
        LocalDate dateMin = LocalDate.now().plusDays(7);
        if (dateCour.isBefore(dateMin)) {
            throw new BadRequestRestException(
                    "La date du cours doit être au moins 7 jours calendaires après aujourd'hui (au plus tôt le "
                            + dateMin + ")");
        }

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

        // 3. Vérifie que l'enseignant est apte au niveau du cours
        //    Règle : niveau_expertise de l'enseignant >= niveau_cible du cours
        int niveauCible;
        try {
            niveauCible = Integer.parseInt(request.getNiveau_cible());
        } catch (NumberFormatException e) {
            throw new BadRequestRestException(
                    "Le niveau cible doit être un entier (1-5). Reçu : " + request.getNiveau_cible());
        }
        if (user.getNiveau_expertise() == null || user.getNiveau_expertise() < niveauCible) {
            throw new ForbiddenRestException(
                    "L'enseignant n'est pas apte au niveau " + niveauCible
                            + ". Niveau d'expertise actuel : " + user.getNiveau_expertise());
        }

        // 4. Envoie la création à ms-cours (en liste de 1 car le endpoint est batch)
        CourEntity toSave = mapper.toEntity(request);
        List<CourEntity> saved = courComponent.createCours(List.of(toSave));

        // 4. Retourne le dernier cours créé
        CourEntity lastCreated = saved.get(saved.size() - 1);
        return mapper.toDTO(lastCreated);
    }

    public List<CourDTO> getCoursByEnseignant(String email) {
        return courComponent.getCoursByEnseignantEmail(email).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<CourDTO> getCoursByEleve(String email) {
        // 1. Récupère l'utilisateur depuis ms-user pour connaître son niveau
        UserEntity user;
        try {
            user = userComponent.getMembreByEmail(email);
        } catch (Exception e) {
            log.warn("Utilisateur introuvable : {}", email);
            throw new UserNotFoundException("Utilisateur introuvable : " + email);
        }

        // 2. Vérifie que le niveau d'expertise est défini
        if (user.getNiveau_expertise() == null) {
            throw new BadRequestRestException("Niveau d'expertise non défini pour " + email);
        }

        // 3. Appelle ms-cours avec le niveau (converti en String pour matcher l'API)
        String niveau = String.valueOf(user.getNiveau_expertise());
        return courComponent.getCoursByNiveau(niveau).stream()
                .map(mapper::toDTO)
                .toList();
    }
}
