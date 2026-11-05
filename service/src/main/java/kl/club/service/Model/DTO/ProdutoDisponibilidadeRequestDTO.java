package kl.club.service.Model.DTO;

import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para ativar ou desativar a disponibilidade do produto")
public record ProdutoDisponibilidadeRequestDTO(

        @Schema(description = "true para disponível e false para indisponível", example = "false")
        @NotNull(message = "O campo disponibilidade é obrigatório")
        Boolean disponibilidade
) {
}
