package kl.club.service.Model.DTO;

import kl.club.service.Model.enums.FormaPagamento;
import kl.club.service.Model.enums.Modalidade;
import kl.club.service.Model.enums.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

/** Pedido no histórico. {@code usuario} vem null em pedido sem cadastro. */
@Schema(description = "Pedido no histórico")
public record PedidoHistoricoResponseDTO(
        @Schema(description = "ID do pedido", example = "5")
        Integer idPedido,
        @Schema(description = "Status do pedido", example = "CONCLUIDO")
        StatusPedido statusPedido,
        @Schema(description = "Modalidade do pedido", example = "LOCAL")
        Modalidade modalidade,
        @Schema(description = "Data e hora do pedido (horário de São Paulo)", example = "2026-10-10T16:20:00")
        LocalDateTime dataHora,
        @Schema(description = "Cliente do pedido; null em pedido sem cadastro")
        PedidoUsuarioResponseDTO usuario,
        @Schema(description = "Itens do pedido")
        List<PedidoItemResumoResponseDTO> itens,
        @Schema(description = "Forma de pagamento", example = "VOUCHER")
        FormaPagamento formaPagamento,
        @Schema(description = "Valor total, em reais", example = "37.0")
        BigDecimal valorTotal,
        @Schema(description = "Pontos gerados (0 em pedido sem cliente)", example = "37")
        Integer pontosGerados
) {
}
