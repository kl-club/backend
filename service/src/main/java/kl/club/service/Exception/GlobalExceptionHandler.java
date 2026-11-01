package kl.club.service.Exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.exc.InvalidFormatException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UsuarioCpfExistenteException.class, UsuarioTelefoneExistenteException.class,
            FuncionarioEmailExistenteException.class, CategoriaNomeExistenteException.class})
    public ProblemDetail handleConflito(RuntimeException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler({CredenciaisInvalidasException.class, SenhaAtualInvalidaException.class})
    public ProblemDetail handleCredenciaisInvalidas(RuntimeException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler({UsuarioNaoEncontradoException.class, PedidoNaoEncontradoException.class,
            FuncionarioNaoEncontradoException.class, CategoriaNaoEncontradaException.class})
    public ProblemDetail handleNaoEncontrado(RuntimeException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidacao(MethodArgumentNotValidException exception) {
        Map<String, String> erros = new LinkedHashMap<>();
        for (FieldError erro : exception.getBindingResult().getFieldErrors()) {
            erros.putIfAbsent(erro.getField(), erro.getDefaultMessage());
        }

        return dadosInvalidos("Dados inválidos", erros);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleCorpoInvalido(HttpMessageNotReadableException exception) {
        if (exception.getCause() instanceof InvalidFormatException causa
                && causa.getTargetType().isEnum() && !causa.getPath().isEmpty()) {
            List<JacksonException.Reference> caminho = causa.getPath();
            String campo = caminho.get(caminho.size() - 1).getPropertyName();

            StringJoiner valores = new StringJoiner(", ");
            for (Object valor : causa.getTargetType().getEnumConstants()) {
                valores.add(valor.toString());
            }

            return dadosInvalidos("Dados inválidos", Map.of(campo, "Valor inválido. Use: " + valores));
        }

        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Corpo da requisição inválido");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleParametroInvalido(MethodArgumentTypeMismatchException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "O " + exception.getName() + " é inválido");
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ProblemDetail handleParametroVazio(MissingPathVariableException exception) {
        return ProblemDetail.forStatusAndDetail(exception.getStatusCode(), "O " + exception.getVariableName() + " é inválido");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ProblemDetail> handleMetodoNaoPermitido(HttpRequestMethodNotSupportedException exception) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.METHOD_NOT_ALLOWED, "Método não permitido");
        return ResponseEntity.status(405).headers(exception.getHeaders()).body(problema);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ProblemDetail handleRotaNaoEncontrada(NoResourceFoundException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "Rota não encontrada");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleIntegridade(DataIntegrityViolationException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "Dado já cadastrado");
    }

    private ProblemDetail dadosInvalidos(String mensagem, Map<String, String> erros) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, mensagem);
        problema.setProperty("erros", erros);
        return problema;
    }
}
