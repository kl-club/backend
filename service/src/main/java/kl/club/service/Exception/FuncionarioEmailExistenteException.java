package kl.club.service.Exception;

public class FuncionarioEmailExistenteException extends RuntimeException {
    public FuncionarioEmailExistenteException() {
        super("Email já cadastrado!");
    }
}
