package kl.club.service.Model.DTO;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Item resumido do pedido")
public record PedidoItemResumoResponseDTO(
        @Schema(description = "Nome atual do produto", example = "Bauru")
        String nome,
        @Schema(description = "Quantidade", example = "2")
        Integer quantidade
) {
}
