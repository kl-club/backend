package kl.club.service.Model.DTO;

import kl.club.service.Model.enums.PeriodoDashboard;

import java.math.BigDecimal;

/**
 * KPIs do período. variacaoPedidos vem null quando ontem não teve pedidos;
 * ticketMedio e ticketMedioAnterior vêm null quando o seu dia não teve pedidos.
 */
public record DashboardKpisResponseDTO(
        PeriodoDashboard periodo,
        BigDecimal faturamento,
        Long quantidadePedidos,
        BigDecimal variacaoPedidos,
        BigDecimal ticketMedio,
        BigDecimal ticketMedioAnterior,
        Long clientesAtendidos
) {
}
