package kl.club.service.Exception;

public class CategoriaNomeExistenteException extends RuntimeException {
    public CategoriaNomeExistenteException() {
        super("Categoria já cadastrada!");
    }
}
