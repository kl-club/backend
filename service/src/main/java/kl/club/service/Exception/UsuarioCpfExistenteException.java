package kl.club.service.Exception;

public class UsuarioCpfExistenteException extends RuntimeException {
    public UsuarioCpfExistenteException() {
        super("CPF já cadastrado!");
    }
}
