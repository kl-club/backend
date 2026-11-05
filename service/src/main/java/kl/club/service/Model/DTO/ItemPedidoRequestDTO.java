package kl.club.service.Model.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Item do pedido a ser criado")
public record ItemPedidoRequestDTO(

        @Schema(description = "ID do produto. Precisa existir e estar disponível", example = "18")
        @NotNull(message = "O produto é obrigatório")
        Integer idProduto,

        @Schema(description = "Quantidade, de 1 a 99", example = "2")
        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 1, message = "A quantidade deve ser no mínimo 1")
        @Max(value = 99, message = "A quantidade deve ser no máximo 99")
        Integer quantidade
) {
}
