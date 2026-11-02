package kl.club.service.Exception;

public class ProdutoNomeExistenteException extends RuntimeException {
    public ProdutoNomeExistenteException() {
        super("Produto já cadastrado!");
    }
}
