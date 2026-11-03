package kl.club.service.Model.DTO;

import kl.club.service.Model.enums.PeriodoDashboard;

import java.math.BigDecimal;
import java.util.List;

public record DashboardPagamentosResponseDTO(
        PeriodoDashboard periodo,
        BigDecimal faturamento,
        List<FormaPagamentoResumoResponseDTO> formasPagamento
) {
}
