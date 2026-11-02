package kl.club.service.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kl.club.service.Model.DTO.CategoriaAtivoRequestDTO;
import kl.club.service.Model.DTO.CategoriaRequestDTO;
import kl.club.service.Model.DTO.CategoriaResponseDTO;
import kl.club.service.Model.DTO.ErroResponseDTO;
import kl.club.service.Service.CategoriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Categorias",
        description = "Gerenciamento das categorias de produtos do KL Club."
)
@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }
    @Operation(
            summary = "Listar categorias",
            description = """
                    Retorna todas as categorias cadastradas,
                    incluindo categorias ativas e inativas.

                    Se não houver categorias, retorna uma lista vazia.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Categorias listadas com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            array = @io.swagger.v3.oas.annotations.media.ArraySchema(
                                    schema = @Schema(
                                            implementation = CategoriaResponseDTO.class
                                    )
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listarCategorias() {
        return ResponseEntity.status(200).body(categoriaService.listar());
    }

    @Operation(
            summary = "Cadastrar categoria",
            description = """
                    Cadastra uma nova categoria no KL Club.

                    Regras:
                    - Nome obrigatório.
                    - Máximo de 100 caracteres.
                    - Não permite nomes já cadastrados.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Categoria cadastrada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = CategoriaResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados de cadastro inválidos",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(
                                    implementation = ErroResponseDTO.class
                            ),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "type": "about:blank",
                                              "title": "Bad Request",
                                              "status": 400,
                                              "detail": "Dados inválidos",
                                              "instance": "/categorias",
                                              "erros": {
                                                "nome": "O nome é obrigatório"
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Categoria já cadastrada",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(
                                    implementation = ErroResponseDTO.class
                            ),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "type": "about:blank",
                                              "title": "Conflict",
                                              "status": 409,
                                              "detail": "Categoria já cadastrada!",
                                              "instance": "/categorias"
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> cadastrarCategoria(@RequestBody @Valid CategoriaRequestDTO requestDTO) {
        CategoriaResponseDTO categoriaCadastrada = categoriaService.cadastrar(requestDTO);
        return ResponseEntity.status(201).body(categoriaCadastrada);
    }

    @Operation(
            summary = "Atualizar categoria",
            description = """
                    Atualiza o nome de uma categoria existente.

                    O novo nome não pode estar em uso por
                    outra categoria.

                    O campo ativo não é alterado.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Categoria atualizada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = CategoriaResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados de atualização inválidos",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(
                                    implementation = ErroResponseDTO.class
                            ),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "type": "about:blank",
                                              "title": "Bad Request",
                                              "status": 400,
                                              "detail": "Dados inválidos",
                                              "instance": "/categorias/1",
                                              "erros": {
                                                "nome": "O nome é obrigatório"
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Categoria não encontrada",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(
                                    implementation = ErroResponseDTO.class
                            ),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "type": "about:blank",
                                              "title": "Not Found",
                                              "status": 404,
                                              "detail": "Categoria não encontrada",
                                              "instance": "/categorias/999"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Nome de categoria já cadastrado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(
                                    implementation = ErroResponseDTO.class
                            ),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "type": "about:blank",
                                              "title": "Conflict",
                                              "status": 409,
                                              "detail": "Categoria já cadastrada!",
                                              "instance": "/categorias/1"
                                            }
                                            """
                            )
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> atualizarCategoria(
            @PathVariable Integer id,
            @RequestBody @Valid CategoriaRequestDTO requestDTO) {
        return ResponseEntity.status(200).body(categoriaService.atualizar(id, requestDTO));
    }



    @Operation(
            summary = "Ativar ou desativar categoria",
            description = """
                    Altera o status de uma categoria.

                    true: ativa a categoria.
                    false: desativa a categoria.

                    O nome da categoria é preservado.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Status atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = CategoriaResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Valor de ativo inválido ou ausente",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(
                                    implementation = ErroResponseDTO.class
                            ),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "type": "about:blank",
                                              "title": "Bad Request",
                                              "status": 400,
                                              "detail": "Dados inválidos",
                                              "instance": "/categorias/1/ativo",
                                              "erros": {
                                                "ativo": "O campo ativo é obrigatório"
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Categoria não encontrada",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(
                                    implementation = ErroResponseDTO.class
                            ),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "type": "about:blank",
                                              "title": "Not Found",
                                              "status": 404,
                                              "detail": "Categoria não encontrada",
                                              "instance": "/categorias/999/ativo"
                                            }
                                            """
                            )
                    )
            )
    })
    @PatchMapping("/{id}/ativo")
    public ResponseEntity<CategoriaResponseDTO> alterarAtivo(
            @Parameter(
                    description = "Identificador da categoria que terá o status alterado",
                    example = "1",
                    required = true
            )
            @PathVariable Integer id,
            @RequestBody @Valid CategoriaAtivoRequestDTO requestDTO) {
        return ResponseEntity.status(200).body(categoriaService.alterarAtivo(id, requestDTO));
    }
}
