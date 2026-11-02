package kl.club.service.Model.DTO;

import jakarta.validation.constraints.NotNull;

public record ProdutoDisponibilidadeRequestDTO(

        @NotNull(message = "O campo disponibilidade é obrigatório")
        Boolean disponibilidade
) {
}
