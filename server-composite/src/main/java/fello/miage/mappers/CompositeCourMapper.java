package fello.miage.mappers;

import fello.miage.modeles.CourEntity;
import fello.miage.requests.CreateCourRequest;
import fello.miage.responses.CourDTO;
import org.springframework.stereotype.Component;

@Component
public class CompositeCourMapper {

    public CourEntity toEntity(CreateCourRequest request) {
        return CourEntity.builder()
                .titre(request.getTitre())
                .niveau_cible(request.getNiveau_cible())
                .debut(request.getDebut())
                .duree(request.getDuree())
                .lieu(request.getLieu())
                .enseignant_email(request.getEmail())
                .build();
    }

    public CourDTO toDTO(CourEntity entity) {
        return CourDTO.builder()
                .id_cour(entity.getId_cour())
                .titre(entity.getTitre())
                .niveau_cible(entity.getNiveau_cible())
                .debut(entity.getDebut())
                .duree(entity.getDuree())
                .lieu(entity.getLieu())
                .enseignant_email(entity.getEnseignant_email())
                .build();
    }
}
