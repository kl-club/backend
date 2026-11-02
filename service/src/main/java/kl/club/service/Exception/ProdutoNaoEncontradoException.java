package kl.club.service.Exception;

public class ProdutoNaoEncontradoException extends RuntimeException {
    public ProdutoNaoEncontradoException() {
        super("Produto não encontrado");
    }
}
