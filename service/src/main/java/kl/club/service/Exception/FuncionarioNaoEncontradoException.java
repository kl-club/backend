package kl.club.service.Exception;

public class FuncionarioNaoEncontradoException extends NegocioException {
    public FuncionarioNaoEncontradoException() {
        super("FUNCIONARIO_NAO_ENCONTRADO", "Funcionário não encontrado");
    }
}
