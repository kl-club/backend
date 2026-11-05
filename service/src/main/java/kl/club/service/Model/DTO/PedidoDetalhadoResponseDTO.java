package kl.club.service.Model.DTO;

import kl.club.service.Model.enums.FormaPagamento;
import kl.club.service.Model.enums.Modalidade;
import kl.club.service.Model.enums.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Pedido do cliente, com os itens e os dados atuais dos produtos")
public record PedidoDetalhadoResponseDTO(
        @Schema(description = "ID do pedido", example = "4")
        Integer idPedido,
        @Schema(description = "Data e hora do pedido (horário de São Paulo)", example = "2026-10-07T10:05:00")
        LocalDateTime dataHora,
        @Schema(description = "Modalidade do pedido", example = "RETIRADA")
        Modalidade modalidade,
        @Schema(description = "Status do pedido", example = "CONCLUIDO")
        StatusPedido statusPedido,
        @Schema(description = "Forma de pagamento", example = "DEBITO")
        FormaPagamento formaPagamento,
        @Schema(description = "Valor total, em reais", example = "31.0")
        BigDecimal valorTotal,
        @Schema(description = "Pontos gerados pelo pedido (0 em pedido sem cliente)", example = "31")
        Integer pontosGerados,
        @Schema(description = "Observações do cliente; pode vir null", example = "Sem cebola, por favor.")
        String observacoes,
        @Schema(description = "Itens do pedido")
        List<ItemPedidoResponseDTO> itens
) {
}