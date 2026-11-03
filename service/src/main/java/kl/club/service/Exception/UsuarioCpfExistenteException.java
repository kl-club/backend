package kl.club.service.Exception;

public class UsuarioCpfExistenteException extends NegocioException {
    public UsuarioCpfExistenteException() {
        super("USUARIO_CPF_EXISTENTE", "CPF já cadastrado!");
    }
}
