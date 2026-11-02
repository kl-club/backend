package kl.club.service.Model.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados para alteração do status da categoria")
public record CategoriaAtivoRequestDTO(

        @Schema(
                description = "true para ativar e false para desativar",
                example = "true"
        )
        @NotNull(message = "O campo ativo é obrigatório")
        Boolean ativo

) {}
