package kl.club.service.Exception;

public class UsuarioTelefoneExistenteException extends NegocioException {
    public UsuarioTelefoneExistenteException() {
        super("USUARIO_TELEFONE_EXISTENTE", "Telefone já cadastrado!");
    }
}
