package kl.club.service.Exception;

public class TransicaoStatusInvalidaException extends NegocioException {
    public TransicaoStatusInvalidaException(String mensagem) {
        super("TRANSICAO_INVALIDA", mensagem);
    }
}
