package kl.club.service.Model.DTO;

import kl.club.service.Model.enums.FormaPagamento;

import java.math.BigDecimal;

/** Valor de uma forma de pagamento no período. O percentual é sobre o valor, com 1 casa. */
public record FormaPagamentoResumoResponseDTO(
        FormaPagamento formaPagamento,
        BigDecimal valorTotal,
        BigDecimal percentual
) {
}
