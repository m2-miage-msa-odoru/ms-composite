package fello.miage.services;

import fello.miage.components.CompetitionComponent;
import fello.miage.components.UserComponent;
import fello.miage.enums.RoleMembre;
import fello.miage.exceptions.rest.BadRequestRestException;
import fello.miage.exceptions.rest.ForbiddenRestException;
import fello.miage.exceptions.technical.UserNotFoundException;
import fello.miage.mappers.CompositeCompetitionMapper;
import fello.miage.modeles.CompetitionEntity;
import fello.miage.modeles.ResultatEntity;
import fello.miage.modeles.UserEntity;
import fello.miage.requests.CreateCompetitionRequest;
import fello.miage.requests.NoterCompetitionRequest;
import fello.miage.responses.CompetitionDTO;
import fello.miage.responses.ResultatDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CompositeCompetitionService {

    private final UserComponent userComponent;
    private final CompetitionComponent competitionComponent;
    private final CompositeCompetitionMapper mapper;

    public CompetitionDTO createCompetition(CreateCompetitionRequest request) {
        // 0. Règle métier : la date de la compétition doit être au moins J+7 (jours calendaires)
        if (request.getDebut() == null) {
            throw new BadRequestRestException("La date de début de la compétition est obligatoire");
        }
        LocalDate dateCompetition = request.getDebut().toLocalDate();
        LocalDate dateMin = LocalDate.now().plusDays(7);
        if (dateCompetition.isBefore(dateMin)) {
            throw new BadRequestRestException(
                    "La date de la compétition doit être au moins 7 jours calendaires après aujourd'hui (au plus tôt le "
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
                    "Seul un ENSEIGNANT peut créer une compétition. Rôle actuel : " + user.getRole());
        }

        // 3. Vérifie que l'enseignant est apte au niveau de la compétition
        //    Règle : niveau_expertise de l'enseignant >= niveau_cible de la compétition
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

        // 4. Envoie la création à ms-competition (en liste de 1 car le endpoint est batch)
        CompetitionEntity toSave = mapper.toEntity(request);
        List<CompetitionEntity> saved = competitionComponent.createCompetitions(List.of(toSave));

        // 5. Retourne la dernière compétition créée
        CompetitionEntity lastCreated = saved.get(saved.size() - 1);
        return mapper.toDTO(lastCreated);
    }

    public List<CompetitionDTO> getCompetitionsByEnseignant(String email) {
        return competitionComponent.getCompetitionsByEnseignantEmail(email).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<CompetitionDTO> getCompetitionsByEleve(String email) {
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

        // 3. Appelle ms-competition avec le niveau (converti en String pour matcher l'API)
        String niveau = String.valueOf(user.getNiveau_expertise());
        return competitionComponent.getCompetitionsByNiveau(niveau).stream()
                .map(mapper::toDTO)
                .toList();
    }

    /**
     * Attribue une note (sur 10) à un adhérent pour une compétition.
     * Règles vérifiées :
     *  - l'enseignant a le rôle ENSEIGNANT ;
     *  - le niveau d'expertise de l'enseignant est >= au niveau de la compétition ;
     *  - l'adhérent a exactement le niveau de la compétition.
     * L'email de l'enseignant n'est pas persisté : il sert uniquement à ces vérifications.
     */
    public ResultatDTO noterMembre(NoterCompetitionRequest request) {
        // 1. Récupère la compétition (404 si introuvable) pour connaître son niveau cible
        CompetitionEntity competition = competitionComponent.getCompetitionById(request.getId_competition());
        int niveauCompetition;
        try {
            niveauCompetition = Integer.parseInt(competition.getNiveau_cible());
        } catch (NumberFormatException e) {
            throw new BadRequestRestException(
                    "Le niveau cible de la compétition est invalide : " + competition.getNiveau_cible());
        }

        // 2. Vérifie l'enseignant : existence, rôle ENSEIGNANT et niveau >= niveau de la compétition
        UserEntity enseignant;
        try {
            enseignant = userComponent.getMembreByEmail(request.getEnseignant_email());
        } catch (Exception e) {
            log.warn("Enseignant introuvable : {}", request.getEnseignant_email());
            throw new UserNotFoundException("Enseignant introuvable : " + request.getEnseignant_email());
        }
        if (enseignant.getRole() != RoleMembre.ENSEIGNANT) {
            throw new ForbiddenRestException(
                    "Seul un ENSEIGNANT peut noter un adhérent. Rôle actuel : " + enseignant.getRole());
        }
        if (enseignant.getNiveau_expertise() == null || enseignant.getNiveau_expertise() < niveauCompetition) {
            throw new ForbiddenRestException(
                    "L'enseignant n'est pas apte au niveau " + niveauCompetition
                            + ". Niveau d'expertise actuel : " + enseignant.getNiveau_expertise());
        }

        // 3. Vérifie l'adhérent : existence et niveau EXACTEMENT égal au niveau de la compétition
        UserEntity adherent;
        try {
            adherent = userComponent.getMembreByEmail(request.getAdherent_email());
        } catch (Exception e) {
            log.warn("Adhérent introuvable : {}", request.getAdherent_email());
            throw new UserNotFoundException("Adhérent introuvable : " + request.getAdherent_email());
        }
        if (adherent.getNiveau_expertise() == null
                || adherent.getNiveau_expertise() != niveauCompetition) {
            throw new ForbiddenRestException(
                    "L'adhérent doit avoir exactement le niveau de la compétition (" + niveauCompetition
                            + "). Niveau d'expertise actuel : " + adherent.getNiveau_expertise());
        }

        // 4. Enregistre la note dans ms-competition (l'email de l'enseignant n'est pas persisté)
        ResultatEntity toSave = ResultatEntity.builder()
                .id_competition(request.getId_competition())
                .adherent_email(request.getAdherent_email())
                .note(request.getNote())
                .build();
        ResultatEntity saved = competitionComponent.enregistrerResultat(toSave);

        return mapper.toDTO(saved);
    }
}
