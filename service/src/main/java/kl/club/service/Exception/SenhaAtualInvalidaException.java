package kl.club.service.Exception;

public class SenhaAtualInvalidaException extends RuntimeException {
    public SenhaAtualInvalidaException() {
        super("Senha atual inválida");
    }
}
