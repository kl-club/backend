package kl.club.service.Model.DTO;

import kl.club.service.Model.enums.FormaPagamento;
import kl.club.service.Model.enums.Modalidade;
import kl.club.service.Model.enums.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/** Pedido no histórico. {@code usuario} vem null em pedido sem cadastro. */
public record PedidoHistoricoResponseDTO(
        Integer idPedido,
        StatusPedido statusPedido,
        Modalidade modalidade,
        LocalDateTime dataHora,
        PedidoUsuarioResponseDTO usuario,
        List<PedidoItemResumoResponseDTO> itens,
        FormaPagamento formaPagamento,
        BigDecimal valorTotal,
        Integer pontosGerados
) {
}
