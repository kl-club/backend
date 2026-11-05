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
import kl.club.service.Model.DTO.ProdutoDisponibilidadeRequestDTO;
import kl.club.service.Model.DTO.ProdutoRequestDTO;
import kl.club.service.Model.DTO.ProdutoResponseDTO;
import kl.club.service.Service.ProdutoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Produtos",
        description = "Cardápio: cadastro, consulta e disponibilidade dos produtos do KL Club."
)
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @Operation(
            summary = "Listar produtos",
            description = """
                    Retorna os produtos cadastrados, com a categoria de cada um.

                    Filtros opcionais e combináveis:
                    - categoria: ID da categoria.
                    - disponibilidade: true para só os disponíveis, false para só os indisponíveis.

                    Produtos de categoria inativa também são devolvidos; a resposta traz categoria.ativo.
                    Se nada for encontrado, retorna uma lista vazia.
                    """
    )
    @Parameter(name = "categoria", in = ParameterIn.QUERY, description = "ID da categoria", example = "9")
    @Parameter(name = "disponibilidade", in = ParameterIn.QUERY, description = "true: só disponíveis; false: só indisponíveis", example = "true")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Produtos listados com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProdutoResponseDTO.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Filtro com tipo inválido",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.PRODUTO_PARAMETRO_INVALIDO)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listarProdutos(
            @RequestParam(name = "categoria", required = false) Integer categoria,
            @RequestParam(name = "disponibilidade", required = false) Boolean disponibilidade) {
        return ResponseEntity.status(200).body(produtoService.listar(categoria, disponibilidade));
    }

    @Operation(
            summary = "Buscar produto",
            description = "Retorna um produto pelo ID, com a categoria."
    )
    @Parameter(name = "id", in = ParameterIn.PATH, description = "ID do produto", example = "18", required = true)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Produto encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Produto não encontrado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.PRODUTO_NAO_ENCONTRADO)
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarProduto(@PathVariable("id") Integer id) {
        return ResponseEntity.status(200).body(produtoService.buscar(id));
    }

    @Operation(
            summary = "Cadastrar produto",
            description = """
                    Cadastra um novo produto. O produto nasce disponível.

                    Regras:
                    - Categoria, nome, preço e pontuação são obrigatórios.
                    - Nome com até 100 caracteres e único entre os produtos
                      (a comparação ignora maiúsculas e acentos).
                    - Preço maior que zero, com até 2 casas decimais.
                    - Pontuação maior ou igual a zero, informada no cadastro (não é calculada pelo preço).
                    - A categoria precisa existir; pode estar inativa.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Produto cadastrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados de cadastro inválidos",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.PRODUTO_DADOS_INVALIDOS)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Categoria não encontrada",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.PRODUTO_CATEGORIA_NAO_ENCONTRADA)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Nome de produto já cadastrado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.PRODUTO_NOME_EXISTENTE)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> cadastrarProduto(@RequestBody @Valid ProdutoRequestDTO requestDTO) {
        ProdutoResponseDTO produtoCadastrado = produtoService.cadastrar(requestDTO);
        return ResponseEntity.status(201).body(produtoCadastrado);
    }

    @Operation(
            summary = "Atualizar produto",
            description = """
                    Atualiza todos os dados de um produto existente, com as mesmas regras do cadastro.

                    O novo nome não pode estar em uso por outro produto.
                    A disponibilidade não é alterada (use PATCH /produtos/{id}/disponibilidade).
                    Pedidos já feitos mantêm o preço e a pontuação da compra.
                    """
    )
    @Parameter(name = "id", in = ParameterIn.PATH, description = "ID do produto", example = "18", required = true)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Produto atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados de atualização inválidos",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.PRODUTO_DADOS_INVALIDOS)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Produto ou categoria não encontrados",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = {
                                    @ExampleObject(name = "Produto não encontrado", value = SwaggerExemplos.PRODUTO_NAO_ENCONTRADO),
                                    @ExampleObject(name = "Categoria não encontrada", value = SwaggerExemplos.PRODUTO_CATEGORIA_NAO_ENCONTRADA)
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Nome de produto já cadastrado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.PRODUTO_NOME_EXISTENTE)
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizarProduto(
            @PathVariable("id") Integer id,
            @RequestBody @Valid ProdutoRequestDTO requestDTO) {
        return ResponseEntity.status(200).body(produtoService.atualizar(id, requestDTO));
    }

    @Operation(
            summary = "Ativar ou desativar produto",
            description = """
                    Altera a disponibilidade de um produto.

                    true: o produto pode ser pedido.
                    false: o produto fica indisponível, e pedidos com ele são recusados (409 PRODUTO_INDISPONIVEL).

                    Não existe DELETE de produto: os itens de pedido referenciam o produto.
                    """
    )
    @Parameter(name = "id", in = ParameterIn.PATH, description = "ID do produto", example = "18", required = true)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Disponibilidade atualizada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Valor de disponibilidade inválido ou ausente",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.PRODUTO_DISPONIBILIDADE_INVALIDA)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Produto não encontrado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.PRODUTO_NAO_ENCONTRADO)
                    )
            )
    })
    @PatchMapping("/{id}/disponibilidade")
    public ResponseEntity<ProdutoResponseDTO> alterarDisponibilidade(
            @PathVariable("id") Integer id,
            @RequestBody @Valid ProdutoDisponibilidadeRequestDTO requestDTO) {
        return ResponseEntity.status(200).body(produtoService.alterarDisponibilidade(id, requestDTO));
    }
}
