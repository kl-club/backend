package kl.club.service.Exception;

public class FuncionarioNaoEncontradoException extends RuntimeException {
    public FuncionarioNaoEncontradoException() {
        super("Funcionário não encontrado");
    }
}
