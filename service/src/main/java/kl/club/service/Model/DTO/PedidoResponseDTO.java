package kl.club.service.Model.DTO;

import kl.club.service.Model.enums.FormaPagamento;
import kl.club.service.Model.enums.Modalidade;
import kl.club.service.Model.enums.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Pedido devolvido pelas rotas de /pedidos (criação, consulta, gestão, avançar e cancelar).
 * {@code usuario} vem null em pedido sem cadastro.
 */
@Schema(description = "Pedido completo, devolvido pelas rotas de /pedidos")
public record PedidoResponseDTO(
        @Schema(description = "ID do pedido (também é o número mostrado ao cliente)", example = "19")
        Integer idPedido,
        @Schema(description = "Data e hora do pedido (horário de São Paulo)", example = "2026-11-05T12:30:00")
        LocalDateTime dataHora,
        @Schema(description = "Modalidade do pedido", example = "LOCAL")
        Modalidade modalidade,
        @Schema(description = "Status do pedido", example = "PAGO")
        StatusPedido statusPedido,
        @Schema(description = "Forma de pagamento", example = "PIX")
        FormaPagamento formaPagamento,
        @Schema(description = "Valor total, em reais", example = "32.0")
        BigDecimal valorTotal,
        @Schema(description = "Pontos gerados (0 em pedido sem cliente)", example = "33")
        Integer pontosGerados,
        @Schema(description = "Observações do cliente; pode vir null", example = "Sem orégano, por favor.")
        String observacoes,
        @Schema(description = "Cliente do pedido; null em pedido sem cadastro")
        PedidoUsuarioResponseDTO usuario,
        @Schema(description = "Itens do pedido")
        List<PedidoItemResponseDTO> itens
) {
}
