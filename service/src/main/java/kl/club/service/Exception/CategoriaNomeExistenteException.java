package kl.club.service.Exception;

public class CategoriaNomeExistenteException extends NegocioException {
    public CategoriaNomeExistenteException() {
        super("CATEGORIA_NOME_EXISTENTE", "Categoria já cadastrada!");
    }
}
