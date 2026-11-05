package kl.club.service.Model.DTO;

import kl.club.service.Model.enums.FormaPagamento;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

/** Valor de uma forma de pagamento no período. O percentual é sobre o valor, com 1 casa. */
@Schema(description = "Valor de uma forma de pagamento no período")
public record FormaPagamentoResumoResponseDTO(
        @Schema(description = "Forma de pagamento", example = "PIX")
        FormaPagamento formaPagamento,
        @Schema(description = "Soma dos pedidos com essa forma, em reais", example = "81.0")
        BigDecimal valorTotal,
        @Schema(description = "Percentual sobre o faturamento, com 1 casa decimal", example = "36.5")
        BigDecimal percentual
) {
}
