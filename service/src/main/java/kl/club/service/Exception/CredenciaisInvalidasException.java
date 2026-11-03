package kl.club.service.Exception;

public class CredenciaisInvalidasException extends NegocioException {
    public CredenciaisInvalidasException() {
        super("CREDENCIAIS_INVALIDAS", "Email ou senha inválidos");
    }
}
