package fello.miage.services;

import fello.miage.components.BadgeComponent;
import fello.miage.components.CourComponent;
import fello.miage.components.UserComponent;
import fello.miage.enums.RoleMembre;
import fello.miage.exceptions.rest.BadRequestRestException;
import fello.miage.exceptions.rest.ForbiddenRestException;
import fello.miage.exceptions.rest.NotFoundRestException;
import fello.miage.exceptions.technical.UserNotFoundException;
import fello.miage.mappers.CompositeBadgeMapper;
import fello.miage.modeles.BadgeEntity;
import fello.miage.modeles.BadgeageEntity;
import fello.miage.modeles.CourEntity;
import fello.miage.modeles.UserEntity;
import fello.miage.requests.AssocierBadgeRequest;
import fello.miage.requests.BadgerRequest;
import fello.miage.requests.CreateBadgeRequest;
import fello.miage.responses.BadgeDTO;
import fello.miage.responses.BadgeageDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class CompositeBadgeService {

    private final UserComponent userComponent;
    private final BadgeComponent badgeComponent;
    private final CourComponent courComponent;
    private final CompositeBadgeMapper mapper;

    /**
     * Enregistre un badge :
     *  1. récupère l'utilisateur (secrétaire) via ms-user ;
     *  2. vérifie qu'il a bien le rôle SECRETAIRE ;
     *  3. appelle ms-badge pour enregistrer le numéro, avec un propriétaire (email_utilisateur) null.
     */
    public BadgeDTO enregistrerBadge(CreateBadgeRequest request) {
        // 1. Récupère la secrétaire depuis ms-user
        UserEntity secretaire;
        try {
            secretaire = userComponent.getMembreByEmail(request.getSecretaire_email());
        } catch (Exception e) {
            log.warn("Secrétaire introuvable : {}", request.getSecretaire_email());
            throw new UserNotFoundException("Secrétaire introuvable : " + request.getSecretaire_email());
        }

        // 2. Vérifie le rôle SECRETAIRE
        if (secretaire.getRole() != RoleMembre.SECRETAIRE) {
            throw new ForbiddenRestException(
                    "Seul un SECRETAIRE peut enregistrer un badge. Rôle actuel : " + secretaire.getRole());
        }

        // 3. Appelle ms-badge (le propriétaire email_utilisateur reste null à la création)
        BadgeEntity saved = badgeComponent.enregistrerBadge(
                BadgeEntity.builder().numero(request.getNumero()).build());

        return mapper.toDTO(saved);
    }

    /**
     * Associe un adhérent comme propriétaire d'un badge :
     *  1. vérifie que l'opérateur a le rôle SECRETAIRE (ms-user) ;
     *  2. vérifie que l'adhérent existe (ms-user) ;
     *  3. met à jour le propriétaire du badge via ms-badge (par numéro).
     */
    public BadgeDTO associerProprietaire(AssocierBadgeRequest request) {
        // 1. Récupère la secrétaire et vérifie son rôle
        UserEntity secretaire;
        try {
            secretaire = userComponent.getMembreByEmail(request.getSecretaire_email());
        } catch (Exception e) {
            log.warn("Secrétaire introuvable : {}", request.getSecretaire_email());
            throw new UserNotFoundException("Secrétaire introuvable : " + request.getSecretaire_email());
        }
        if (secretaire.getRole() != RoleMembre.SECRETAIRE) {
            throw new ForbiddenRestException(
                    "Seul un SECRETAIRE peut associer un badge. Rôle actuel : " + secretaire.getRole());
        }

        // 2. Vérifie que l'adhérent (futur propriétaire) existe
        try {
            userComponent.getMembreByEmail(request.getAdherent_email());
        } catch (Exception e) {
            log.warn("Adhérent introuvable : {}", request.getAdherent_email());
            throw new UserNotFoundException("Adhérent introuvable : " + request.getAdherent_email());
        }

        // 3. Met à jour le propriétaire du badge via ms-badge
        BadgeEntity updated = badgeComponent.associerProprietaire(
                BadgeEntity.builder()
                        .numero(request.getNumero())
                        .email_utilisateur(request.getAdherent_email())
                        .build());

        return mapper.toDTO(updated);
    }

    /**
     * Simule l'action de badger d'un adhérent à un cours :
     *  1. vérifie que le badge existe et qu'il a un propriétaire (ms-badge) ; récupère l'email du propriétaire ;
     *  2. vérifie que le cours existe (ms-cours) et récupère son niveau ;
     *  3. compare le niveau du cours au niveau d'expertise du propriétaire (ms-user) — ils doivent être égaux ;
     *  4. enregistre le badgeage dans ms-badge.
     */
    public BadgeageDTO badger(BadgerRequest request) {
        // 1. Vérifie le badge et récupère le propriétaire
        BadgeEntity badge;
        try {
            badge = badgeComponent.getBadgeByNumero(request.getNumero());
        } catch (Exception e) {
            log.warn("Badge introuvable : numéro {}", request.getNumero());
            throw new NotFoundRestException("Badge introuvable : numéro " + request.getNumero());
        }
        if (badge.getEmail_utilisateur() == null || badge.getEmail_utilisateur().isBlank()) {
            throw new BadRequestRestException(
                    "Le badge numéro " + request.getNumero() + " n'a pas de propriétaire.");
        }

        // 2. Vérifie que le cours existe et récupère son niveau
        CourEntity cours;
        try {
            cours = courComponent.getCoursById(request.getId_cours());
        } catch (Exception e) {
            log.warn("Cours introuvable : {}", request.getId_cours());
            throw new NotFoundRestException("Cours introuvable : " + request.getId_cours());
        }

        // 3. Récupère le niveau d'expertise du propriétaire et le compare au niveau du cours
        UserEntity proprietaire;
        try {
            proprietaire = userComponent.getMembreByEmail(badge.getEmail_utilisateur());
        } catch (Exception e) {
            log.warn("Propriétaire introuvable : {}", badge.getEmail_utilisateur());
            throw new NotFoundRestException("Propriétaire introuvable : " + badge.getEmail_utilisateur());
        }

        int niveauCours;
        try {
            niveauCours = Integer.parseInt(cours.getNiveau_cible());
        } catch (NumberFormatException e) {
            throw new BadRequestRestException("Niveau du cours invalide : " + cours.getNiveau_cible());
        }
        if (proprietaire.getNiveau_expertise() == null || proprietaire.getNiveau_expertise() != niveauCours) {
            throw new BadRequestRestException(
                    "Le niveau du cours (" + niveauCours + ") doit être identique au niveau d'expertise de l'adhérent ("
                            + proprietaire.getNiveau_expertise() + ").");
        }

        // 4. Simule l'action de badger : enregistre le badgeage dans ms-badge
        BadgeageEntity saved = badgeComponent.enregistrerBadgeage(
                BadgeageEntity.builder()
                        .numero_badge(request.getNumero())
                        .id_cours(request.getId_cours())
                        .build());

        return mapper.toDTO(saved);
    }
}
