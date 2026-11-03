package kl.club.service.Exception;

/**
 * Base das exceções de negócio. O {@code codigo} vai na resposta de erro
 * para o front distinguir os casos sem depender do texto da mensagem.
 */
public abstract class NegocioException extends RuntimeException {

    private final String codigo;

    protected NegocioException(String codigo, String mensagem) {
        super(mensagem);
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
