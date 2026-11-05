package kl.club.service.Model.DTO;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados completos do produto")
public record ProdutoResponseDTO(

        @Schema(description = "ID do produto", example = "18")
        Integer idProduto,
        @Schema(description = "Categoria do produto, com o status dela (produto de categoria inativa continua sendo devolvido)")
        CategoriaResponseDTO categoria,
        @Schema(description = "Nome do produto", example = "Bauru")
        String nome,
        @Schema(description = "Descrição do produto; pode vir null", example = "Lanche prensado.")
        String descricao,
        @Schema(description = "Ingredientes do produto; pode vir null", example = "Pão francês, presunto, queijo mussarela, tomate, orégano, manteiga")
        String ingredientes,
        @Schema(description = "Preço em reais", example = "9.5")
        BigDecimal preco,
        @Schema(description = "Pontos por unidade", example = "10")
        Integer pontuacao,
        @Schema(description = "URL da imagem; pode vir null", example = "https://cdn.klclub.com.br/produtos/bauru.jpg")
        String imagem,
        @Schema(description = "true se o produto pode ser pedido", example = "true")
        Boolean disponibilidade
) {
}
