package kl.club.service.Exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
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

    private static final String DADOS_INVALIDOS = "DADOS_INVALIDOS";
    private static final String PARAMETRO_INVALIDO = "PARAMETRO_INVALIDO";

    @ExceptionHandler({UsuarioCpfExistenteException.class, UsuarioTelefoneExistenteException.class,
            FuncionarioEmailExistenteException.class, CategoriaNomeExistenteException.class,
            ProdutoNomeExistenteException.class, ProdutoIndisponivelException.class,
            TransicaoStatusInvalidaException.class})
    public ProblemDetail handleConflito(NegocioException exception) {
        return erro(HttpStatus.CONFLICT, exception);
    }

    @ExceptionHandler({CredenciaisInvalidasException.class, SenhaAtualInvalidaException.class})
    public ProblemDetail handleCredenciaisInvalidas(NegocioException exception) {
        return erro(HttpStatus.UNAUTHORIZED, exception);
    }

    @ExceptionHandler({UsuarioNaoEncontradoException.class, PedidoNaoEncontradoException.class,
            FuncionarioNaoEncontradoException.class, CategoriaNaoEncontradaException.class,
            ProdutoNaoEncontradoException.class})
    public ProblemDetail handleNaoEncontrado(NegocioException exception) {
        return erro(HttpStatus.NOT_FOUND, exception);
    }

    @ExceptionHandler({ProdutoRepetidoException.class, ParametroInvalidoException.class})
    public ProblemDetail handleRequisicaoInvalida(NegocioException exception) {
        return erro(HttpStatus.BAD_REQUEST, exception);
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

        return erro(HttpStatus.BAD_REQUEST, "Corpo da requisição inválido", DADOS_INVALIDOS);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleParametroInvalido(MethodArgumentTypeMismatchException exception) {
        return erro(HttpStatus.BAD_REQUEST, "O " + exception.getName() + " é inválido", PARAMETRO_INVALIDO);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ProblemDetail handleParametroAusente(MissingServletRequestParameterException exception) {
        return erro(HttpStatus.BAD_REQUEST, "O parâmetro " + exception.getParameterName() + " é obrigatório",
                PARAMETRO_INVALIDO);
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ProblemDetail handleParametroVazio(MissingPathVariableException exception) {
        return erro(exception.getStatusCode(), "O " + exception.getVariableName() + " é inválido", PARAMETRO_INVALIDO);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ProblemDetail> handleMetodoNaoPermitido(HttpRequestMethodNotSupportedException exception) {
        ProblemDetail problema = erro(HttpStatus.METHOD_NOT_ALLOWED, "Método não permitido", "METODO_NAO_PERMITIDO");
        return ResponseEntity.status(405).headers(exception.getHeaders()).body(problema);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ProblemDetail handleRotaNaoEncontrada(NoResourceFoundException exception) {
        return erro(HttpStatus.NOT_FOUND, "Rota não encontrada", "ROTA_NAO_ENCONTRADA");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleIntegridade(DataIntegrityViolationException exception) {
        return erro(HttpStatus.CONFLICT, "Dado já cadastrado", "DADO_JA_CADASTRADO");
    }

    private ProblemDetail erro(HttpStatusCode status, NegocioException exception) {
        return erro(status, exception.getMessage(), exception.getCodigo());
    }

    private ProblemDetail erro(HttpStatusCode status, String mensagem, String codigo) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(status, mensagem);
        problema.setProperty("codigo", codigo);
        return problema;
    }

    private ProblemDetail dadosInvalidos(String mensagem, Map<String, String> erros) {
        ProblemDetail problema = erro(HttpStatus.BAD_REQUEST, mensagem, DADOS_INVALIDOS);
        problema.setProperty("erros", erros);
        return problema;
    }
}
