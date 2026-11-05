package kl.club.service.Model.DTO;

import kl.club.service.Model.enums.PeriodoDashboard;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ranking de produtos mais vendidos no período")
public record DashboardProdutosResponseDTO(
        @Schema(description = "Período consultado", example = "HOJE")
        PeriodoDashboard periodo,
        @Schema(description = "Até 5 produtos, do mais vendido para o menos vendido, com desempate pelo nome")
        List<ProdutoVendidoResponseDTO> produtos
) {
}
