package kl.club.service.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kl.club.service.Config.SwaggerExemplos;
import kl.club.service.Model.DTO.ErroResponseDTO;
import jakarta.validation.Valid;
import kl.club.service.Model.DTO.FuncionarioAtualizacaoRequestDTO;
import kl.club.service.Model.DTO.FuncionarioLoginRequestDTO;
import kl.club.service.Model.DTO.FuncionarioRequestDTO;
import kl.club.service.Model.DTO.FuncionarioResponseDTO;
import kl.club.service.Model.DTO.FuncionarioSenhaRequestDTO;
import kl.club.service.Service.FuncionarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Funcionários",
        description = "Acesso ao dashboard: cadastro, atualização, desativação, troca de senha e login dos funcionários."
)
@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @Operation(
            summary = "Listar funcionários",
            description = """
                    Retorna todos os funcionários, ativos e desativados. A senha nunca é devolvida.

                    Se não houver funcionários, retorna uma lista vazia.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Funcionários listados com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = FuncionarioResponseDTO.class))
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<FuncionarioResponseDTO>> listarFuncionarios() {
        return ResponseEntity.status(200).body(funcionarioService.listar());
    }

    @Operation(
            summary = "Cadastrar funcionário",
            description = """
                    Cadastra um funcionário com acesso ao dashboard. O funcionário nasce ativo.

                    Regras:
                    - Nome, e-mail, senha e nível obrigatórios.
                    - E-mail válido e único entre os funcionários.
                    - Nível ADMINISTRADOR ou OPERACIONAL. Qualquer nível é aceito, sem controle de permissão.
                    - A senha é gravada em texto puro, por decisão do projeto, e nunca é devolvida.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Funcionário cadastrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FuncionarioResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados de cadastro inválidos (inclui nível fora de ADMINISTRADOR e OPERACIONAL)",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.FUNCIONARIO_DADOS_INVALIDOS)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "E-mail já cadastrado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.FUNCIONARIO_EMAIL_EXISTENTE)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<FuncionarioResponseDTO> cadastrarFuncionario(@RequestBody @Valid FuncionarioRequestDTO requestDTO) {
        FuncionarioResponseDTO funcionarioCadastrado = funcionarioService.cadastrar(requestDTO);
        return ResponseEntity.status(201).body(funcionarioCadastrado);
    }

    @Operation(
            summary = "Atualizar funcionário",
            description = """
                    Atualiza nome, e-mail e nível de um funcionário.

                    O novo e-mail não pode estar em uso por outro funcionário.
                    A senha e o status (ativo) não são alterados por esta rota.
                    """
    )
    @Parameter(name = "id", in = ParameterIn.PATH, description = "ID do funcionário", example = "1", required = true)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Funcionário atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FuncionarioResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados de atualização inválidos",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.FUNCIONARIO_DADOS_INVALIDOS)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Funcionário não encontrado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.FUNCIONARIO_NAO_ENCONTRADO)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "E-mail já cadastrado para outro funcionário",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.FUNCIONARIO_EMAIL_EXISTENTE)
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDTO> atualizarFuncionario(
            @PathVariable Integer id,
            @RequestBody @Valid FuncionarioAtualizacaoRequestDTO requestDTO) {
        return ResponseEntity.status(200).body(funcionarioService.atualizar(id, requestDTO));
    }

    @Operation(
            summary = "Desativar funcionário",
            description = """
                    Falso delete: o funcionário continua cadastrado, com ativo = false.

                    Funcionário desativado não consegue mais fazer login (401 CREDENCIAIS_INVALIDAS).
                    Não há rota de reativação. Desativar um funcionário já desativado também responde 204.
                    """
    )
    @Parameter(name = "id", in = ParameterIn.PATH, description = "ID do funcionário", example = "3", required = true)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Funcionário desativado com sucesso (sem corpo)"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Funcionário não encontrado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.FUNCIONARIO_NAO_ENCONTRADO)
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativarFuncionario(@PathVariable Integer id) {
        funcionarioService.desativar(id);
        return ResponseEntity.status(204).build();
    }

    @Operation(
            summary = "Trocar senha",
            description = """
                    Troca a senha do funcionário. Exige a senha atual para confirmar.

                    Responde 204, sem corpo.
                    """
    )
    @Parameter(name = "id", in = ParameterIn.PATH, description = "ID do funcionário", example = "1", required = true)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Senha alterada com sucesso (sem corpo)"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Senha atual ou nova senha ausentes ou inválidas",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.FUNCIONARIO_SENHA_DADOS_INVALIDOS)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Senha atual não confere",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.FUNCIONARIO_SENHA_ATUAL_INVALIDA)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Funcionário não encontrado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.FUNCIONARIO_NAO_ENCONTRADO)
                    )
            )
    })
    @PatchMapping("/{id}/senha")
    public ResponseEntity<Void> trocarSenha(
            @PathVariable Integer id,
            @RequestBody @Valid FuncionarioSenhaRequestDTO requestDTO) {
        funcionarioService.trocarSenha(id, requestDTO);
        return ResponseEntity.status(204).build();
    }

    @Operation(
            summary = "Login de funcionário",
            description = """
                    Confere e-mail e senha e devolve os dados do funcionário. Não gera token:
                    a API não tem autenticação nas rotas, e a sessão fica a cargo do front.

                    E-mail inexistente, senha errada e funcionário desativado retornam
                    a mesma resposta (401 CREDENCIAIS_INVALIDAS).
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Credenciais válidas",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FuncionarioResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "E-mail ou senha ausentes",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.FUNCIONARIO_LOGIN_DADOS_INVALIDOS)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "E-mail ou senha inválidos, ou funcionário desativado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.FUNCIONARIO_CREDENCIAIS_INVALIDAS)
                    )
            )
    })
    @PostMapping("/login")
    public ResponseEntity<FuncionarioResponseDTO> login(@RequestBody @Valid FuncionarioLoginRequestDTO requestDTO) {
        return ResponseEntity.status(200).body(funcionarioService.login(requestDTO));
    }
}
