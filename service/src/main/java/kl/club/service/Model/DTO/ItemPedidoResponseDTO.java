package kl.club.service.Model.DTO;

import java.math.BigDecimal;

public record ItemPedidoResponseDTO(
        Integer idItemPedido,
        Integer quantidade,
        BigDecimal precoUnitario,
        Integer pontosUnitario,
        ProdutoResumoResponseDTO produto
) {
}
