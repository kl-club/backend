package kl.club.service.Model.DTO;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Produto resumido, usado nos itens dos pedidos do cliente")
public record ProdutoResumoResponseDTO(
        @Schema(description = "ID do produto", example = "18")
        Integer idProduto,
        @Schema(description = "Categoria do produto")
        CategoriaResumoResponseDTO categoria,
        @Schema(description = "Nome atual do produto", example = "Bauru")
        String nome,
        @Schema(description = "Descrição do produto; pode vir null", example = "Lanche prensado.")
        String descricao,
        @Schema(description = "Ingredientes do produto; pode vir null", example = "Pão francês, presunto, queijo mussarela, tomate, orégano, manteiga")
        String ingredientes,
        @Schema(description = "Preço atual do produto, em reais", example = "9.5")
        BigDecimal preco,
        @Schema(description = "Pontuação atual do produto", example = "10")
        Integer pontuacao,
        @Schema(description = "URL da imagem; pode vir null", example = "https://cdn.klclub.com.br/produtos/bauru.jpg")
        String imagem
) {
}