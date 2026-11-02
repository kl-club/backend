package kl.club.service.Model.Mapper;

import kl.club.service.Model.DTO.CategoriaResumoResponseDTO;
import kl.club.service.Model.DTO.ProdutoResumoResponseDTO;
import kl.club.service.Model.Entity.Categoria;
import kl.club.service.Model.Entity.Produto;

public final class ProdutoMapper {

    private ProdutoMapper() {
    }

    public static ProdutoResumoResponseDTO toResumoResponse(Produto produto) {

        Categoria categoria = produto.getCategoria();

        CategoriaResumoResponseDTO categoriaDTO =
                new CategoriaResumoResponseDTO(
                        categoria.getIdCategoria(),
                        categoria.getNome(),
                        categoria.getAtivo()
                );

        return new ProdutoResumoResponseDTO(
                produto.getIdProduto(),
                categoriaDTO,
                produto.getNome(),
                produto.getDescricao(),
                produto.getIngredientes(),
                produto.getPreco(),
                produto.getPontuacao(),
                produto.getImagem()
        );
    }
}