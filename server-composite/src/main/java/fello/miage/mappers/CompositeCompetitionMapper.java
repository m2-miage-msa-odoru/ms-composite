package fello.miage.mappers;

import fello.miage.modeles.CompetitionEntity;
import fello.miage.modeles.ResultatEntity;
import fello.miage.requests.CreateCompetitionRequest;
import fello.miage.responses.CompetitionDTO;
import fello.miage.responses.ResultatDTO;
import org.springframework.stereotype.Component;

@Component
public class CompositeCompetitionMapper {

    public CompetitionEntity toEntity(CreateCompetitionRequest request) {
        return CompetitionEntity.builder()
                .titre(request.getTitre())
                .niveau_cible(request.getNiveau_cible())
                .debut(request.getDebut())
                .duree(request.getDuree())
                .lieu(request.getLieu())
                .enseignant_email(request.getEmail())
                .build();
    }

    public CompetitionDTO toDTO(CompetitionEntity entity) {
        return CompetitionDTO.builder()
                .id_competition(entity.getId_competition())
                .titre(entity.getTitre())
                .niveau_cible(entity.getNiveau_cible())
                .debut(entity.getDebut())
                .duree(entity.getDuree())
                .lieu(entity.getLieu())
                .enseignant_email(entity.getEnseignant_email())
                .build();
    }

    public ResultatDTO toDTO(ResultatEntity entity) {
        return ResultatDTO.builder()
                .id_resultat(entity.getId_resultat())
                .id_competition(entity.getId_competition())
                .adherent_email(entity.getAdherent_email())
                .note(entity.getNote())
                .build();
    }
}
