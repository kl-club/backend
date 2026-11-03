package kl.club.service.Model.DTO;

import kl.club.service.Model.enums.FormaPagamento;
import kl.club.service.Model.enums.StatusPedido;

import java.math.BigDecimal;
import java.util.List;

/** Linha da tabela de pedidos do dashboard. {@code nomeUsuario} vem null em pedido sem cliente. */
public record PedidoDashboardResponseDTO(
        Integer idPedido,
        StatusPedido statusPedido,
        String nomeUsuario,
        List<PedidoItemResumoResponseDTO> itens,
        FormaPagamento formaPagamento,
        BigDecimal valorTotal,
        Integer pontosGerados
) {
}
