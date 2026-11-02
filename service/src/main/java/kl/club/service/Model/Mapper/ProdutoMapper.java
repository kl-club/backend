package kl.club.service.Model.Mapper;

import kl.club.service.Model.DTO.CategoriaResumoResponseDTO;
import kl.club.service.Model.DTO.ProdutoRequestDTO;
import kl.club.service.Model.DTO.ProdutoResponseDTO;
import kl.club.service.Model.DTO.ProdutoResumoResponseDTO;
import kl.club.service.Model.Entity.Categoria;
import kl.club.service.Model.Entity.Produto;

import java.util.ArrayList;
import java.util.List;

public final class ProdutoMapper {

    private ProdutoMapper() {
    }

    public static ProdutoResponseDTO toResponse(Produto produto) {
        return new ProdutoResponseDTO(
                produto.getIdProduto(),
                CategoriaMapper.toResponse(produto.getCategoria()),
                produto.getNome(),
                produto.getDescricao(),
                produto.getIngredientes(),
                produto.getPreco(),
                produto.getPontuacao(),
                produto.getImagem(),
                produto.getDisponibilidade());
    }

    public static List<ProdutoResponseDTO> toResponse(List<Produto> produtos) {

        List<ProdutoResponseDTO> produtosResponse = new ArrayList<>();

        for (Produto produto : produtos) {
            produtosResponse.add(toResponse(produto));
        }

        return produtosResponse;
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

    public static Produto toEntity(ProdutoRequestDTO dto, Categoria categoria) {

        Produto produto = new Produto();

        produto.setCategoria(categoria);
        produto.setNome(dto.nome());
        produto.setDescricao(dto.descricao());
        produto.setIngredientes(dto.ingredientes());
        produto.setPreco(dto.preco());
        produto.setPontuacao(dto.pontuacao());
        produto.setImagem(dto.imagem());

        return produto;
    }
}
