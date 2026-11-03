package kl.club.service.Model.DTO;

/** Produto no ranking de mais vendidos. {@code imagem} pode vir null. */
public record ProdutoVendidoResponseDTO(
        Integer posicao,
        Integer idProduto,
        String nome,
        String imagem,
        Long quantidadeVendida
) {
}
