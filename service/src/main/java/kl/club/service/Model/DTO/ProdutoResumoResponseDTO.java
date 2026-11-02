package kl.club.service.Model.DTO;

import java.math.BigDecimal;

public record ProdutoResumoResponseDTO(
        Integer idProduto,
        CategoriaResumoResponseDTO categoria,
        String nome,
        String descricao,
        String ingredientes,
        BigDecimal preco,
        Integer pontuacao,
        String imagem
) {
}