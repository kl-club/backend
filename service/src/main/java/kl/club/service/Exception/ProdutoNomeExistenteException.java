package kl.club.service.Exception;

public class ProdutoNomeExistenteException extends NegocioException {
    public ProdutoNomeExistenteException() {
        super("PRODUTO_NOME_EXISTENTE", "Produto já cadastrado!");
    }
}
