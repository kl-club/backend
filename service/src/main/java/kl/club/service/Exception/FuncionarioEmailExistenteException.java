package kl.club.service.Exception;

public class FuncionarioEmailExistenteException extends NegocioException {
    public FuncionarioEmailExistenteException() {
        super("FUNCIONARIO_EMAIL_EXISTENTE", "Email já cadastrado!");
    }
}
