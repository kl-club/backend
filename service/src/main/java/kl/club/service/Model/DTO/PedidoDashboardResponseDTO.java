package kl.club.service.Model.DTO;

import kl.club.service.Model.enums.FormaPagamento;
import kl.club.service.Model.enums.StatusPedido;

import java.math.BigDecimal;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

/** Linha da tabela de pedidos do dashboard. {@code nomeUsuario} vem null em pedido sem cliente. */
@Schema(description = "Linha da tabela de pedidos do dashboard")
public record PedidoDashboardResponseDTO(
        @Schema(description = "ID do pedido", example = "18")
        Integer idPedido,
        @Schema(description = "Status do pedido (cancelados também aparecem)", example = "PAGO")
        StatusPedido statusPedido,
        @Schema(description = "Nome do cliente; null em pedido sem cadastro", example = "Lucas Martins")
        String nomeUsuario,
        @Schema(description = "Itens do pedido")
        List<PedidoItemResumoResponseDTO> itens,
        @Schema(description = "Forma de pagamento", example = "CREDITO")
        FormaPagamento formaPagamento,
        @Schema(description = "Valor total, em reais", example = "22.5")
        BigDecimal valorTotal,
        @Schema(description = "Pontos gerados (0 em pedido sem cliente)", example = "23")
        Integer pontosGerados
) {
}
