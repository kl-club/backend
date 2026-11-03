package kl.club.service.Exception;

public class ProdutoRepetidoException extends NegocioException {
    public ProdutoRepetidoException(Integer idProduto) {
        super("PRODUTO_REPETIDO", "O produto " + idProduto
                + " aparece mais de uma vez no pedido. Informe cada produto uma única vez, com a quantidade desejada");
    }
}
