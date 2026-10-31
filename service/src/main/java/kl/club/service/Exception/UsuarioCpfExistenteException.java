package kl.club.service.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "CPF já cadastrado!")
public class UsuarioCpfExistenteException extends RuntimeException {
    public UsuarioCpfExistenteException(String cpf) {
        super("Já existe um usuário com este CPF: " + cpf);
    }
}
