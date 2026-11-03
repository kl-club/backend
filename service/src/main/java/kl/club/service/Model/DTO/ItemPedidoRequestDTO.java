package kl.club.service.Model.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemPedidoRequestDTO(

        @NotNull(message = "O produto é obrigatório")
        Integer idProduto,

        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 1, message = "A quantidade deve ser no mínimo 1")
        @Max(value = 99, message = "A quantidade deve ser no máximo 99")
        Integer quantidade
) {
}
