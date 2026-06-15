package fello.miage.mappers;

import fello.miage.modeles.BadgeEntity;
import fello.miage.modeles.BadgeageEntity;
import fello.miage.responses.BadgeDTO;
import fello.miage.responses.BadgeageDTO;
import org.springframework.stereotype.Component;

@Component
public class CompositeBadgeMapper {

    public BadgeDTO toDTO(BadgeEntity entity) {
        return BadgeDTO.builder()
                .id(entity.getId())
                .numero(entity.getNumero())
                .email_utilisateur(entity.getEmail_utilisateur())
                .build();
    }

    public BadgeageDTO toDTO(BadgeageEntity entity) {
        return BadgeageDTO.builder()
                .id(entity.getId())
                .numero_badge(entity.getNumero_badge())
                .id_cours(entity.getId_cours())
                .email_utilisateur(entity.getEmail_utilisateur())
                .build();
    }
}
