package kl.club.service.Exception;

public class SenhaAtualInvalidaException extends NegocioException {
    public SenhaAtualInvalidaException() {
        super("SENHA_ATUAL_INVALIDA", "Senha atual inválida");
    }
}
