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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Tests unitaires de CompositeCourService (ms-composite).
 * Couvre les 3 méthodes du service : createCour, getCoursByEnseignant, getCoursByEleve.
 */
@ExtendWith(MockitoExtension.class)
class CompositeCourServiceTest {

    @Mock
    private UserComponent userComponent;

    @Mock
    private CourComponent courComponent;

    @Mock
    private CompositeCourMapper mapper;

    @InjectMocks
    private CompositeCourService service;

    private UserEntity enseignantNiveau3;
    private UserEntity adherent;
    private CreateCourRequest validRequest;
    private CourEntity savedEntity;
    private CourDTO dto;

    @BeforeEach
    void setUp() {
        enseignantNiveau3 = UserEntity.builder()
                .email("alice@callme.fr")
                .nom("Dupont")
                .prenom("Alice")
                .role(RoleMembre.ENSEIGNANT)
                .niveau_expertise(3)
                .build();

        adherent = UserEntity.builder()
                .email("bob@callme.fr")
                .nom("Martin")
                .prenom("Bob")
                .role(RoleMembre.ADHERENT)
                .niveau_expertise(1)
                .build();

        validRequest = CreateCourRequest.builder()
                .email("alice@callme.fr")
                .titre("Cha-cha-cha")
                .niveau_cible("2")
                .debut(LocalDateTime.now().plusDays(10)) // J+10 > J+7 OK
                .duree(60)
                .lieu("Salle A")
                .build();

        savedEntity = CourEntity.builder()
                .id_cour("abc-123")
                .titre("Cha-cha-cha")
                .niveau_cible("2")
                .duree(60)
                .lieu("Salle A")
                .enseignant_email("alice@callme.fr")
                .build();

        dto = CourDTO.builder()
                .id_cour("abc-123")
                .titre("Cha-cha-cha")
                .niveau_cible("2")
                .duree(60)
                .lieu("Salle A")
                .enseignant_email("alice@callme.fr")
                .build();
    }

    // ── createCour : cas nominal ────────────────────────────────

    @Test
    void createCour_casNominal_devraitCreerEtRetournerLeDTO() {
        when(userComponent.getMembreByEmail("alice@callme.fr")).thenReturn(enseignantNiveau3);
        when(mapper.toEntity(any(CreateCourRequest.class))).thenReturn(savedEntity);
        when(courComponent.createCours(any())).thenReturn(List.of(savedEntity));
        when(mapper.toDTO(savedEntity)).thenReturn(dto);

        CourDTO result = service.createCour(validRequest);

        assertThat(result).isNotNull();
        assertThat(result.getId_cour()).isEqualTo("abc-123");
        assertThat(result.getEnseignant_email()).isEqualTo("alice@callme.fr");
    }

    // ── createCour : règles métier ──────────────────────────────

    @Test
    void createCour_devraitRefuser_siDateAvantJplus7() {
        validRequest.setDebut(LocalDateTime.now().plusDays(3));

        assertThatThrownBy(() -> service.createCour(validRequest))
                .isInstanceOf(BadRequestRestException.class)
                .hasMessageContaining("7 jours");
    }

    @Test
    void createCour_devraitRefuser_siDateNulle() {
        validRequest.setDebut(null);

        assertThatThrownBy(() -> service.createCour(validRequest))
                .isInstanceOf(BadRequestRestException.class)
                .hasMessageContaining("obligatoire");
    }

    @Test
    void createCour_devraitRefuser_siUtilisateurInconnu() {
        when(userComponent.getMembreByEmail(anyString()))
                .thenThrow(new RuntimeException("404"));

        assertThatThrownBy(() -> service.createCour(validRequest))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("introuvable");
    }

    @Test
    void createCour_devraitRefuser_siUtilisateurPasEnseignant() {
        validRequest.setEmail("bob@callme.fr");
        when(userComponent.getMembreByEmail("bob@callme.fr")).thenReturn(adherent);

        assertThatThrownBy(() -> service.createCour(validRequest))
                .isInstanceOf(ForbiddenRestException.class)
                .hasMessageContaining("ENSEIGNANT");
    }

    @Test
    void createCour_devraitRefuser_siEnseignantPasApteAuNiveau() {
        validRequest.setNiveau_cible("5"); // Alice est niveau 3
        when(userComponent.getMembreByEmail("alice@callme.fr")).thenReturn(enseignantNiveau3);

        assertThatThrownBy(() -> service.createCour(validRequest))
                .isInstanceOf(ForbiddenRestException.class)
                .hasMessageContaining("apte au niveau 5");
    }

    // ── getCoursByEnseignant ────────────────────────────────────

    @Test
    void getCoursByEnseignant_devraitRetournerListeDeDTOs() {
        when(courComponent.getCoursByEnseignantEmail("alice@callme.fr")).thenReturn(List.of(savedEntity));
        when(mapper.toDTO(savedEntity)).thenReturn(dto);

        List<CourDTO> result = service.getCoursByEnseignant("alice@callme.fr");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEnseignant_email()).isEqualTo("alice@callme.fr");
    }

    // ── getCoursByEleve ─────────────────────────────────────────

    @Test
    void getCoursByEleve_devraitAppelerMsUserPuisMsCoursAvecLeBonNiveau() {
        when(userComponent.getMembreByEmail("alice@callme.fr")).thenReturn(enseignantNiveau3);
        when(courComponent.getCoursByNiveau("3")).thenReturn(List.of(savedEntity));
        when(mapper.toDTO(savedEntity)).thenReturn(dto);

        List<CourDTO> result = service.getCoursByEleve("alice@callme.fr");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNiveau_cible()).isEqualTo("2"); // dto.niveau_cible
    }

    @Test
    void getCoursByEleve_devraitLancerUserNotFound_siEmailInconnu() {
        when(userComponent.getMembreByEmail(anyString()))
                .thenThrow(new RuntimeException("404"));

        assertThatThrownBy(() -> service.getCoursByEleve("inconnu@callme.fr"))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("introuvable");
    }

    @Test
    void getCoursByEleve_devraitLancerBadRequest_siNiveauNull() {
        UserEntity sansNiveau = UserEntity.builder()
                .email("alice@callme.fr")
                .niveau_expertise(null)
                .build();
        when(userComponent.getMembreByEmail("alice@callme.fr")).thenReturn(sansNiveau);

        assertThatThrownBy(() -> service.getCoursByEleve("alice@callme.fr"))
                .isInstanceOf(BadRequestRestException.class)
                .hasMessageContaining("Niveau d'expertise non défini");
    }
}
