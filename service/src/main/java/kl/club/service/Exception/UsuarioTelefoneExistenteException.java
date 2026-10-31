package kl.club.service.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus (code = HttpStatus.CONFLICT, reason = "Telefone já cadastrado!")
public class UsuarioTelefoneExistenteException extends RuntimeException {
    public UsuarioTelefoneExistenteException(String telefone) {
        super("Já existe um usuário com este telefone: "+ telefone);
    }
}