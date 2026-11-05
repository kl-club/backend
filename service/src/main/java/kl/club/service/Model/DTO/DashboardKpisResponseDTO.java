package kl.club.service.Model.DTO;

import kl.club.service.Model.enums.PeriodoDashboard;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * KPIs do período. variacaoPedidos vem null quando ontem não teve pedidos;
 * ticketMedio e ticketMedioAnterior vêm null quando o seu dia não teve pedidos.
 */
@Schema(description = "Indicadores do período. Pedidos CANCELADO ficam de fora")
public record DashboardKpisResponseDTO(
        @Schema(description = "Período consultado", example = "HOJE")
        PeriodoDashboard periodo,
        @Schema(description = "Soma do valor dos pedidos do período, em reais", example = "222.0")
        BigDecimal faturamento,
        @Schema(description = "Quantidade de pedidos do período", example = "9")
        Long quantidadePedidos,
        @Schema(description = "Variação percentual sobre o dia anterior inteiro; null quando ontem não teve pedidos", example = "350.0")
        BigDecimal variacaoPedidos,
        @Schema(description = "Valor médio por pedido no período; null sem pedidos", example = "24.67")
        BigDecimal ticketMedio,
        @Schema(description = "Ticket médio do dia anterior; null quando ontem não teve pedidos", example = "31.5")
        BigDecimal ticketMedioAnterior,
        @Schema(description = "Clientes cadastrados distintos mais os pedidos sem cadastro do período", example = "8")
        Long clientesAtendidos
) {
}
