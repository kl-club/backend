package kl.club.service.Model.DTO;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Item do pedido")
public record PedidoItemResponseDTO(
        @Schema(description = "ID do produto", example = "18")
        Integer idProduto,
        @Schema(description = "Nome atual do produto", example = "Bauru")
        String nome,
        @Schema(description = "Quantidade", example = "2")
        Integer quantidade,
        @Schema(description = "Preço unitário na hora da compra, em reais", example = "9.5")
        BigDecimal precoUnitario,
        @Schema(description = "Preço unitário vezes a quantidade, em reais", example = "19.0")
        BigDecimal valorTotal
) {
}
