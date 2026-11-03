package kl.club.service.Model.DTO;

import java.math.BigDecimal;

public record PedidoItemResponseDTO(
        Integer idProduto,
        String nome,
        Integer quantidade,
        BigDecimal precoUnitario,
        BigDecimal valorTotal
) {
}
