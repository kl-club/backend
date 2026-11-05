package kl.club.service.Model.DTO;

import kl.club.service.Model.enums.PeriodoDashboard;

import java.math.BigDecimal;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Distribuição do faturamento por forma de pagamento")
public record DashboardPagamentosResponseDTO(
        @Schema(description = "Período consultado", example = "HOJE")
        PeriodoDashboard periodo,
        @Schema(description = "Faturamento do período, em reais", example = "222.0")
        BigDecimal faturamento,
        @Schema(description = "Formas usadas no período, do maior para o menor valor")
        List<FormaPagamentoResumoResponseDTO> formasPagamento
) {
}
