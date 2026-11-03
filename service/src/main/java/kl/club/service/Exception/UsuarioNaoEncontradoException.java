package kl.club.service.Exception;

public class UsuarioNaoEncontradoException extends NegocioException {
    public UsuarioNaoEncontradoException() {
        super("USUARIO_NAO_ENCONTRADO", "Usuário não encontrado");
    }

    public UsuarioNaoEncontradoException(String message) {
        super("USUARIO_NAO_ENCONTRADO", message);
    }
}
