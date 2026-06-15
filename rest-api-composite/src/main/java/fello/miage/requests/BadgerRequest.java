package fello.miage.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BadgerRequest {

    @Schema(description = "Numéro du badge utilisé pour badger")
    @NotNull
    private Integer numero;

    @Schema(description = "Identifiant du cours suivi")
    @NotBlank
    private String id_cours;
}
