package kl.club.service.Exception;

public class CategoriaNaoEncontradaException extends NegocioException {
    public CategoriaNaoEncontradaException() {
        super("CATEGORIA_NAO_ENCONTRADA", "Categoria não encontrada");
    }
}
