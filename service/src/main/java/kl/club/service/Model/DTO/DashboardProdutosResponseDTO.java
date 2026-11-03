package kl.club.service.Model.DTO;

import kl.club.service.Model.enums.PeriodoDashboard;

import java.util.List;

public record DashboardProdutosResponseDTO(
        PeriodoDashboard periodo,
        List<ProdutoVendidoResponseDTO> produtos
) {
}
