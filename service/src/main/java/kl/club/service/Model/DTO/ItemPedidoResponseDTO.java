package kl.club.service.Model.DTO;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Item de um pedido do cliente")
public record ItemPedidoResponseDTO(
        @Schema(description = "ID do item", example = "7")
        Integer idItemPedido,
        @Schema(description = "Quantidade", example = "1")
        Integer quantidade,
        @Schema(description = "Preço unitário na hora da compra, em reais", example = "16.0")
        BigDecimal precoUnitario,
        @Schema(description = "Pontos por unidade na hora da compra", example = "16")
        Integer pontosUnitario,
        @Schema(description = "Produto, com nome e dados atuais")
        ProdutoResumoResponseDTO produto
) {
}
