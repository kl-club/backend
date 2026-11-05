package kl.club.service.Model.DTO;
import io.swagger.v3.oas.annotations.media.Schema;

/** Produto no ranking de mais vendidos. {@code imagem} pode vir null. */
@Schema(description = "Produto no ranking de mais vendidos")
public record ProdutoVendidoResponseDTO(
        @Schema(description = "Posição no ranking, a partir de 1", example = "1")
        Integer posicao,
        @Schema(description = "ID do produto", example = "18")
        Integer idProduto,
        @Schema(description = "Nome do produto", example = "Bauru")
        String nome,
        @Schema(description = "URL da imagem; pode vir null", example = "https://cdn.klclub.com.br/produtos/bauru.jpg")
        String imagem,
        @Schema(description = "Unidades vendidas no período", example = "3")
        Long quantidadeVendida
) {
}
