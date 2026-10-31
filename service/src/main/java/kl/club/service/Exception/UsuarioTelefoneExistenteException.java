package kl.club.service.Exception;

public class UsuarioTelefoneExistenteException extends RuntimeException {
    public UsuarioTelefoneExistenteException() {
        super("Telefone já cadastrado!");
    }
}
