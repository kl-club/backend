package kl.club.service.Exception;

public class ParametroInvalidoException extends NegocioException {
    public ParametroInvalidoException(String mensagem) {
        super("PARAMETRO_INVALIDO", mensagem);
    }
}
