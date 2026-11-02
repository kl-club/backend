package kl.club.service.Model.DTO;

import java.math.BigDecimal;

public record ProdutoResponseDTO(

        Integer idProduto,
        CategoriaResponseDTO categoria,
        String nome,
        String descricao,
        String ingredientes,
        BigDecimal preco,
        Integer pontuacao,
        String imagem,
        Boolean disponibilidade
) {
}
