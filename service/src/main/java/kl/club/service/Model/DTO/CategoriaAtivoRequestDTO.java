package kl.club.service.Model.DTO;

import jakarta.validation.constraints.NotNull;

public record CategoriaAtivoRequestDTO(

        @NotNull(message = "O campo ativo é obrigatório")
        Boolean ativo
) {
}
