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
import jakarta.validation.Valid;
import kl.club.service.Model.DTO.*;
import kl.club.service.Service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Usuários (clientes)",
        description = """
                Cadastro opcional do cliente, login pelo CPF, pontos e pedidos do cliente.
                """
)
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(
            summary = "Cadastrar cliente",
            description = """
                    Cadastra um cliente.

                    Regras:
                    - Nome obrigatório, até 100 caracteres.
                    - CPF válido, só os 11 dígitos, único.
                    - Telefone só com os 11 dígitos (DDD + número), único.

                    As mensagens de validação desta rota saem em inglês (padrão do Bean Validation).
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Cliente cadastrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados de cadastro inválidos",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.USUARIO_DADOS_INVALIDOS)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "CPF ou telefone já cadastrados",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = {
                                    @ExampleObject(name = "CPF já cadastrado", value = SwaggerExemplos.USUARIO_CPF_EXISTENTE),
                                    @ExampleObject(name = "Telefone já cadastrado", value = SwaggerExemplos.USUARIO_TELEFONE_EXISTENTE)
                            }
                    )
            )
    })
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrarUsuario(@RequestBody @Valid UsuarioRequestDTO requestDTO) {
        UsuarioResponseDTO usuarioCadastrado = usuarioService.cadastrar(requestDTO);
        return ResponseEntity.status(201).body(usuarioCadastrado);
    }

    // Fazer um endpoint para após o cara inserir o valor no campo do frontend,
    // o próprio frontend enviar uma chamada pra api verificar se já existe algum registro com aquele valor
    @Operation(
            summary = "Verificar CPF e telefone",
            description = """
                    Informa se o CPF e o telefone já estão cadastrados, para o front avisar antes do cadastro.

                    Comportamento atual:
                    - Os dois parâmetros são obrigatórios na rota. Sem um deles: 400 PARAMETRO_INVALIDO.
                    - Um deles pode ir vazio (ex.: telefone=). Nesse caso, o campo correspondente vem null.
                    - Os dois vazios: 400 no formato padrão do Spring, sem "codigo".
                    """
    )
    @Parameter(name = "cpf", in = ParameterIn.QUERY, description = "CPF, só os 11 dígitos (pode ir vazio)", example = "12345678909", required = true)
    @Parameter(name = "telefone", in = ParameterIn.QUERY, description = "Telefone, só os 11 dígitos (pode ir vazio)", example = "11987654321", required = true)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Verificação feita",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioVerificacaoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parâmetro ausente, ou os dois vazios",
                    content = {
                            @Content(
                                    mediaType = "application/problem+json",
                                    schema = @Schema(implementation = ErroResponseDTO.class),
                                    examples = @ExampleObject(name = "Parâmetro ausente", value = SwaggerExemplos.USUARIO_VERIFICAR_PARAMETRO_AUSENTE)
                            ),
                            @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(name = "Os dois vazios (sem codigo)", value = SwaggerExemplos.USUARIO_VERIFICAR_SEM_CODIGO)
                            )
                    }
            )
    })
    @GetMapping("/verificar")
    public ResponseEntity<UsuarioVerificacaoResponseDTO> existsByCpfOuTelefone(
          @RequestParam("cpf") String cpf,
         @RequestParam("telefone") String telefone){


        UsuarioVerificacaoResponseDTO response =
                usuarioService.verificar(cpf, telefone);

        return ResponseEntity.status(200).body(response);

    }

    @Operation(
            summary = "Login do cliente",
            description = """
                    Identifica o cliente só pelo CPF (sem senha) e devolve os dados dele. Não gera token.

                    CPF não cadastrado: 404 USUARIO_NAO_ENCONTRADO, sem "detail".
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Cliente encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "CPF ausente ou sem 11 caracteres",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.USUARIO_LOGIN_DADOS_INVALIDOS)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "CPF não cadastrado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.USUARIO_LOGIN_NAO_ENCONTRADO)
                    )
            )
    })
    @PostMapping("/login")
    public ResponseEntity<UsuarioResponseDTO> login(@RequestBody @Valid UsuarioAutenticacaoRequestDTO requestDTO){

        return ResponseEntity.status(200).body(usuarioService.login(requestDTO));
    }


    @Operation(
            summary = "Listar clientes com pedidos",
            description = """
                    Retorna todos os clientes, cada um com todos os seus pedidos e itens
                    (clientes sem pedidos vêm com a lista vazia).

                    Se não houver clientes, retorna uma lista vazia.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Clientes listados com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UsuarioPedidosResponseDTO.class))
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<UsuarioPedidosResponseDTO>> listarTodosComPedidos() {

        List<UsuarioPedidosResponseDTO> usuarios =
                usuarioService.listarTodosComPedidos();

        return ResponseEntity.ok(usuarios);
    }

    @Operation(
            summary = "Buscar cliente",
            description = "Retorna o cliente com todos os seus pedidos e itens."
    )
    @Parameter(name = "id", in = ParameterIn.PATH, description = "ID do cliente", example = "1", required = true)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Cliente encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioPedidosResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cliente não encontrado (sem \"detail\")",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.USUARIO_NAO_ENCONTRADO)
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioPedidosResponseDTO> buscarPorId(
            @PathVariable Integer id
    ) {

        UsuarioPedidosResponseDTO usuario =
                usuarioService.buscarPorId(id);

        return ResponseEntity.ok(usuario);
    }

    @Operation(
            summary = "Atualizar cliente",
            description = """
                    Atualiza o nome e/ou o telefone do cliente. CPF não pode ser alterado.

                    Regras:
                    - Informe pelo menos um dos dois campos. Corpo sem nenhum deles:
                      400 no formato padrão do Spring, sem "codigo".
                    - Nome com até 100 caracteres, sem ficar em branco.
                    - Telefone só com os 11 dígitos e não pode estar em uso por outro cliente.
                    """
    )
    @Parameter(name = "id", in = ParameterIn.PATH, description = "ID do cliente", example = "1", required = true)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Cliente atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos, ou nenhum campo informado",
                    content = {
                            @Content(
                                    mediaType = "application/problem+json",
                                    schema = @Schema(implementation = ErroResponseDTO.class),
                                    examples = @ExampleObject(name = "Dados inválidos", value = SwaggerExemplos.USUARIO_ATUALIZAR_DADOS_INVALIDOS)
                            ),
                            @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(name = "Nenhum campo informado (sem codigo)", value = SwaggerExemplos.USUARIO_ATUALIZAR_SEM_CAMPOS)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cliente não encontrado (sem \"detail\")",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.USUARIO_NAO_ENCONTRADO)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Telefone já cadastrado para outro cliente",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.USUARIO_ATUALIZAR_TELEFONE_EXISTENTE)
                    )
            )
    })
    @PatchMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid UsuarioAtualizacaoRequestDTO dto
    ) {

        UsuarioResponseDTO usuarioAtualizado =
                usuarioService.atualizar(id, dto);

        return ResponseEntity.status(200).body(usuarioAtualizado);
    }


    @Operation(
            summary = "Saldo de pontos",
            description = """
                    Soma os pontos gerados pelos pedidos do cliente que estão fora de PENDENTE e CANCELADO.

                    Os pontos passam a contar quando o pedido é pago e deixam de contar se ele for cancelado.
                    """
    )
    @Parameter(name = "id", in = ParameterIn.PATH, description = "ID do cliente", example = "4", required = true)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Saldo calculado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioPontosResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cliente não encontrado (sem \"detail\")",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.USUARIO_NAO_ENCONTRADO)
                    )
            )
    })
    @GetMapping("/{id}/pontos")
    public ResponseEntity<UsuarioPontosResponseDTO> consultarPontos(
            @PathVariable Integer id
    ) {

        UsuarioPontosResponseDTO response =
                usuarioService.consultarPontos(id);

        return ResponseEntity.status(200).body(response);
    }

    @Operation(
            summary = "Pedidos do cliente",
            description = """
                    Retorna os pedidos do cliente, de todos os status, dos mais recentes para os mais antigos,
                    com os itens e os dados atuais de cada produto.

                    Cliente sem pedidos: lista vazia.
                    """
    )
    @Parameter(name = "id", in = ParameterIn.PATH, description = "ID do cliente", example = "3", required = true)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedidos listados com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PedidoDetalhadoResponseDTO.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cliente não encontrado (sem \"detail\")",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.USUARIO_NAO_ENCONTRADO)
                    )
            )
    })
    @GetMapping("/{id}/pedidos")
    public ResponseEntity<List<PedidoDetalhadoResponseDTO>> listarPedidosPorUsuario(
            @PathVariable Integer id
    ) {

        List<PedidoDetalhadoResponseDTO> pedidos =
                usuarioService.listarPedidosPorUsuario(id);

        return ResponseEntity.status(200).body(pedidos);
    }

    @Operation(
            summary = "Resumo do cliente",
            description = """
                    Números do cliente:
                    - quantidadePedidos: pedidos de todos os status, inclusive cancelados.
                    - totalGasto: soma dos pedidos CONCLUIDO.
                    - pontosAcumulados: mesma regra de GET /usuarios/{id}/pontos.
                    - ultimoPedidoId: pedido mais recente, ou null se não houver.
                    """
    )
    @Parameter(name = "id", in = ParameterIn.PATH, description = "ID do cliente", example = "3", required = true)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Resumo calculado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResumoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cliente não encontrado (sem \"detail\")",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.USUARIO_NAO_ENCONTRADO)
                    )
            )
    })
    @GetMapping("/{id}/resumo")
    public ResponseEntity<UsuarioResumoResponseDTO> buscarResumo(
            @PathVariable Integer id
    ) {

        UsuarioResumoResponseDTO resumo =
                usuarioService.buscarResumo(id);

        return ResponseEntity.ok(resumo);
    }

    @Operation(
            summary = "Pedido do cliente",
            description = """
                    Retorna um pedido do cliente, com os itens e os dados atuais de cada produto.

                    Pedido inexistente ou de outro cliente: 404 no formato padrão do Spring, sem "codigo".
                    """
    )
    @Parameter(name = "id", in = ParameterIn.PATH, description = "ID do cliente", example = "3", required = true)
    @Parameter(name = "pedidoId", in = ParameterIn.PATH, description = "ID do pedido", example = "4", required = true)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedido encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PedidoDetalhadoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cliente não encontrado, ou pedido inexistente / de outro cliente",
                    content = {
                            @Content(
                                    mediaType = "application/problem+json",
                                    schema = @Schema(implementation = ErroResponseDTO.class),
                                    examples = @ExampleObject(name = "Cliente não encontrado", value = SwaggerExemplos.USUARIO_NAO_ENCONTRADO)
                            ),
                            @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(name = "Pedido não encontrado para o cliente (sem codigo)", value = SwaggerExemplos.USUARIO_PEDIDO_NAO_ENCONTRADO)
                            )
                    }
            )
    })
    @GetMapping("/{id}/pedidos/{pedidoId}")
    public ResponseEntity<PedidoDetalhadoResponseDTO> buscarPedidoPorUsuario(
            @PathVariable Integer id,
            @PathVariable Integer pedidoId
    ) {

        PedidoDetalhadoResponseDTO pedido =
                usuarioService.buscarPedidoPorUsuario(id, pedidoId);

        return ResponseEntity.status(200).body(pedido);
    }
}
