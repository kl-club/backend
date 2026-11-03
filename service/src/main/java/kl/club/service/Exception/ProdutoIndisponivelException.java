package kl.club.service.Exception;

public class ProdutoIndisponivelException extends NegocioException {
    public ProdutoIndisponivelException(String nomeProduto) {
        super("PRODUTO_INDISPONIVEL", "O produto " + nomeProduto + " está indisponível");
    }
}
