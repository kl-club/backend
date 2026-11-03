package kl.club.service.Exception;

public class ProdutoNaoEncontradoException extends NegocioException {
    public ProdutoNaoEncontradoException() {
        super("PRODUTO_NAO_ENCONTRADO", "Produto não encontrado");
    }

    public ProdutoNaoEncontradoException(Integer idProduto) {
        super("PRODUTO_NAO_ENCONTRADO", "Produto " + idProduto + " não encontrado");
    }
}
