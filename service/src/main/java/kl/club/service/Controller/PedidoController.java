package kl.club.service.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
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
import kl.club.service.Model.DTO.DashboardKpisResponseDTO;
import kl.club.service.Model.DTO.DashboardPagamentosResponseDTO;
import kl.club.service.Model.DTO.DashboardProdutosResponseDTO;
import kl.club.service.Model.DTO.HistoricoFiltroDTO;
import kl.club.service.Model.DTO.PaginaResponseDTO;
import kl.club.service.Model.DTO.PedidoDashboardResponseDTO;
import kl.club.service.Model.DTO.PedidoHistoricoResponseDTO;
import kl.club.service.Model.DTO.PedidoRequestDTO;
import kl.club.service.Model.DTO.PedidoResponseDTO;
import kl.club.service.Model.enums.Modalidade;
import kl.club.service.Model.enums.PeriodoDashboard;
import kl.club.service.Model.enums.StatusPedido;
import kl.club.service.Service.DashboardService;
import kl.club.service.Service.PedidoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@Tag(
        name = "Pedidos",
        description = "Criação e consulta de pedidos, dashboard, gestão e histórico."
)
@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final DashboardService dashboardService;

    public PedidoController(PedidoService pedidoService, DashboardService dashboardService) {
        this.pedidoService = pedidoService;
        this.dashboardService = dashboardService;
    }

    @Operation(
            summary = "Criar pedido",
            description = """
                    Cria um pedido com os itens informados.

                    Regras:
                    - Cliente opcional (idUsuario). Pedido sem cliente não gera pontos.
                    - De 1 a 50 itens, cada produto uma única vez, com quantidade de 1 a 99.
                    - Produtos precisam existir e estar disponíveis.
                    - Preço, total, pontos, status e data são calculados pelo back-end.
                    - PIX, CREDITO, DEBITO e VOUCHER nascem PAGO; DINHEIRO nasce PENDENTE.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Pedido criado. O header Location aponta para GET /pedidos/{id}",
                    headers = @Header(name = "Location", description = "URL do pedido criado", schema = @Schema(type = "string", example = "http://localhost:8080/pedidos/19")),
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PedidoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos, ou o mesmo produto em mais de um item",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = {
                                    @ExampleObject(name = "Dados inválidos", value = SwaggerExemplos.PEDIDO_DADOS_INVALIDOS),
                                    @ExampleObject(name = "Produto repetido", value = SwaggerExemplos.PEDIDO_PRODUTO_REPETIDO)
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cliente ou produto não encontrados",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = {
                                    @ExampleObject(name = "Cliente não encontrado", value = SwaggerExemplos.PEDIDO_USUARIO_NAO_ENCONTRADO),
                                    @ExampleObject(name = "Produto não encontrado", value = SwaggerExemplos.PEDIDO_PRODUTO_NAO_ENCONTRADO)
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Produto indisponível",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.PEDIDO_PRODUTO_INDISPONIVEL)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<PedidoResponseDTO> criar(@RequestBody @Valid PedidoRequestDTO requestDTO) {
        PedidoResponseDTO pedidoCriado = pedidoService.criar(requestDTO);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(pedidoCriado.idPedido())
                .toUri();

        return ResponseEntity.created(location).body(pedidoCriado);
    }

    @Operation(
            summary = "Buscar pedido",
            description = "Retorna o pedido completo, com cliente e itens."
    )
    @Parameter(name = "id", in = ParameterIn.PATH, description = "ID do pedido", example = "4", required = true)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedido encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PedidoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pedido não encontrado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.PEDIDO_NAO_ENCONTRADO)
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(pedidoService.buscarPorId(id));
    }

    @Operation(
            summary = "Dashboard: KPIs",
            description = """
                    Faturamento, quantidade de pedidos (com variação sobre o dia anterior inteiro),
                    ticket médio (do período e do dia anterior) e clientes atendidos.
                    Pedidos CANCELADO ficam de fora. periodo é opcional e por enquanto só aceita HOJE.
                    """
    )
    @Parameter(name = "periodo", in = ParameterIn.QUERY, description = "Período do dashboard. Por enquanto só HOJE", example = "HOJE")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "KPIs calculados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DashboardKpisResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Período inválido",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.DASHBOARD_PERIODO_INVALIDO)
                    )
            )
    })
    @GetMapping("/dashboard/kpis")
    public ResponseEntity<DashboardKpisResponseDTO> calcularKpis(
            @RequestParam(defaultValue = "HOJE") PeriodoDashboard periodo) {
        return ResponseEntity.status(200).body(dashboardService.calcularKpis(periodo));
    }

    @Operation(
            summary = "Dashboard: tabela de pedidos",
            description = """
                    Todos os pedidos do período, inclusive cancelados, dos mais recentes para os mais antigos.
                    pagina (a partir de 1, padrão 1) e tamanho (1 a 50, padrão 10).
                    """
    )
    @Parameter(name = "periodo", in = ParameterIn.QUERY, description = "Período do dashboard. Por enquanto só HOJE", example = "HOJE")
    @Parameter(name = "pagina", in = ParameterIn.QUERY, description = "Página, a partir de 1", example = "1")
    @Parameter(name = "tamanho", in = ParameterIn.QUERY, description = "Itens por página, de 1 a 50", example = "10")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Página de pedidos (conteudo: lista de pedidos da tabela)"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Período ou paginação inválidos",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.DASHBOARD_PAGINACAO_INVALIDA)
                    )
            )
    })
    @GetMapping("/dashboard/tabela")
    public ResponseEntity<PaginaResponseDTO<PedidoDashboardResponseDTO>> listarPedidosDashboard(
            @RequestParam(defaultValue = "HOJE") PeriodoDashboard periodo,
            @RequestParam(defaultValue = "1") Integer pagina,
            @RequestParam(defaultValue = "10") Integer tamanho) {
        return ResponseEntity.status(200).body(dashboardService.listarPedidos(periodo, pagina, tamanho));
    }

    @Operation(
            summary = "Dashboard: distribuição de pagamentos",
            description = """
                    Valor e percentual sobre o faturamento de cada forma de pagamento usada no período,
                    do maior para o menor valor. Pedidos CANCELADO ficam de fora.
                    """
    )
    @Parameter(name = "periodo", in = ParameterIn.QUERY, description = "Período do dashboard. Por enquanto só HOJE", example = "HOJE")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Distribuição calculada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DashboardPagamentosResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Período inválido",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.DASHBOARD_PERIODO_INVALIDO)
                    )
            )
    })
    @GetMapping("/dashboard/pagamentos")
    public ResponseEntity<DashboardPagamentosResponseDTO> calcularPagamentos(
            @RequestParam(defaultValue = "HOJE") PeriodoDashboard periodo) {
        return ResponseEntity.status(200).body(dashboardService.calcularPagamentos(periodo));
    }

    @Operation(
            summary = "Dashboard: produtos mais vendidos",
            description = """
                    Os 5 produtos com mais unidades vendidas no período, com desempate pelo nome.
                    Pedidos CANCELADO ficam de fora.
                    """
    )
    @Parameter(name = "periodo", in = ParameterIn.QUERY, description = "Período do dashboard. Por enquanto só HOJE", example = "HOJE")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Ranking calculado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DashboardProdutosResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Período inválido",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.DASHBOARD_PERIODO_INVALIDO)
                    )
            )
    })
    @GetMapping("/dashboard/produtos-mais-vendidos")
    public ResponseEntity<DashboardProdutosResponseDTO> listarMaisVendidos(
            @RequestParam(defaultValue = "HOJE") PeriodoDashboard periodo) {
        return ResponseEntity.status(200).body(dashboardService.listarMaisVendidos(periodo));
    }

    @Operation(
            summary = "Histórico de pedidos",
            description = """
                    Lista paginada de pedidos de todos os status, inclusive cancelados.
                    Todos os filtros são opcionais e combináveis.

                    - idPedido: busca exata pelo código do pedido.
                    - nomeUsuario: busca parcial pelo nome do cliente, sem diferenciar maiúsculas e minúsculas.
                    - dataInicio e dataFim (AAAA-MM-DD): ambas inclusivas; com só uma, o intervalo fica aberto do outro lado.
                    - modalidade: LOCAL ou RETIRADA.
                    - ordenarPor=valor e direcao=asc|desc (padrão desc). Sem ordenarPor: mais recentes primeiro.
                    - pagina (a partir de 1, padrão 1) e tamanho (1 a 50, padrão 10).

                    Parâmetro inválido retorna 400 PARAMETRO_INVALIDO.
                    """
    )
    @Parameter(name = "idPedido", in = ParameterIn.QUERY, description = "Código exato do pedido", example = "5")
    @Parameter(name = "nomeUsuario", in = ParameterIn.QUERY, description = "Parte do nome do cliente, sem diferenciar maiúsculas", example = "ana")
    @Parameter(name = "dataInicio", in = ParameterIn.QUERY, description = "Data inicial, inclusiva (AAAA-MM-DD)", example = "2026-10-01")
    @Parameter(name = "dataFim", in = ParameterIn.QUERY, description = "Data final, inclusiva (AAAA-MM-DD)", example = "2026-10-31")
    @Parameter(name = "modalidade", in = ParameterIn.QUERY, description = "LOCAL ou RETIRADA", example = "LOCAL")
    @Parameter(name = "ordenarPor", in = ParameterIn.QUERY, description = "Só aceita valor. Sem ele, mais recentes primeiro", example = "valor")
    @Parameter(name = "direcao", in = ParameterIn.QUERY, description = "asc ou desc (padrão desc). Usado com ordenarPor", example = "desc")
    @Parameter(name = "pagina", in = ParameterIn.QUERY, description = "Página, a partir de 1", example = "1")
    @Parameter(name = "tamanho", in = ParameterIn.QUERY, description = "Itens por página, de 1 a 50", example = "10")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Página do histórico (conteudo: lista de pedidos do histórico)"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Filtro, ordenação ou paginação inválidos",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.HISTORICO_PARAMETRO_INVALIDO)
                    )
            )
    })
    @GetMapping("/historico")
    public ResponseEntity<PaginaResponseDTO<PedidoHistoricoResponseDTO>> listarHistorico(
            @RequestParam(required = false) Integer idPedido,
            @RequestParam(required = false) String nomeUsuario,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(required = false) Modalidade modalidade,
            @RequestParam(required = false) String ordenarPor,
            @RequestParam(required = false) String direcao,
            @RequestParam(defaultValue = "1") Integer pagina,
            @RequestParam(defaultValue = "10") Integer tamanho) {

        HistoricoFiltroDTO filtro = new HistoricoFiltroDTO(idPedido, nomeUsuario, dataInicio, dataFim,
                modalidade, ordenarPor, direcao, pagina, tamanho);

        return ResponseEntity.status(200).body(pedidoService.listarHistorico(filtro));
    }

    @Operation(
            summary = "Gestão: novos pedidos",
            description = "Pedidos de hoje em PENDENTE ou PAGO, mais antigos primeiro."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedidos listados (lista vazia se não houver)",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PedidoResponseDTO.class))
                    )
            )
    })
    @GetMapping("/gestao/novos")
    public ResponseEntity<List<PedidoResponseDTO>> listarNovos() {
        return ResponseEntity.status(200).body(
                pedidoService.listarDoDia(List.of(StatusPedido.PENDENTE, StatusPedido.PAGO)));
    }

    @Operation(
            summary = "Gestão: em preparo",
            description = "Pedidos de hoje em EM_PREPARO, mais antigos primeiro."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedidos listados (lista vazia se não houver)",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PedidoResponseDTO.class))
                    )
            )
    })
    @GetMapping("/gestao/em-preparo")
    public ResponseEntity<List<PedidoResponseDTO>> listarEmPreparo() {
        return ResponseEntity.status(200).body(pedidoService.listarDoDia(List.of(StatusPedido.EM_PREPARO)));
    }

    @Operation(
            summary = "Gestão: prontos para retirada",
            description = "Pedidos de hoje em PRONTO_PARA_RETIRADA, mais antigos primeiro."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedidos listados (lista vazia se não houver)",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PedidoResponseDTO.class))
                    )
            )
    })
    @GetMapping("/gestao/prontos")
    public ResponseEntity<List<PedidoResponseDTO>> listarProntos() {
        return ResponseEntity.status(200).body(pedidoService.listarDoDia(List.of(StatusPedido.PRONTO_PARA_RETIRADA)));
    }

    @Operation(
            summary = "Gestão: concluídos",
            description = "Pedidos de hoje em CONCLUIDO, mais recentes primeiro (pela data do pedido)."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedidos listados (lista vazia se não houver)",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PedidoResponseDTO.class))
                    )
            )
    })
    @GetMapping("/gestao/concluidos")
    public ResponseEntity<List<PedidoResponseDTO>> listarConcluidos() {
        return ResponseEntity.status(200).body(pedidoService.listarConcluidosDoDia());
    }

    @Operation(
            summary = "Avançar status",
            description = """
                    Move o pedido uma etapa adiante. Não recebe corpo: o back-end define o próximo status.

                    PENDENTE ou PAGO → EM_PREPARO → PRONTO_PARA_RETIRADA → CONCLUIDO.
                    Pedido CONCLUIDO ou CANCELADO retorna 409 TRANSICAO_INVALIDA.
                    """
    )
    @Parameter(name = "id", in = ParameterIn.PATH, description = "ID do pedido", example = "10", required = true)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedido avançado; a resposta traz o novo status",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PedidoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pedido não encontrado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.PEDIDO_AVANCAR_NAO_ENCONTRADO)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Pedido já concluído ou cancelado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = {
                                    @ExampleObject(name = "Pedido concluído", value = SwaggerExemplos.PEDIDO_AVANCAR_CONCLUIDO),
                                    @ExampleObject(name = "Pedido cancelado", value = SwaggerExemplos.PEDIDO_AVANCAR_CANCELADO)
                            }
                    )
            )
    })
    @PatchMapping("/{id}/avancar")
    public ResponseEntity<PedidoResponseDTO> avancar(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(pedidoService.avancar(id));
    }

    @Operation(
            summary = "Cancelar pedido",
            description = """
                    Cancela um pedido PENDENTE ou PAGO. Não recebe corpo.
                    Em qualquer outro status, retorna 409 TRANSICAO_INVALIDA.
                    Os pontos do pedido deixam de contar no saldo do cliente. Não há estorno de pagamento.
                    """
    )
    @Parameter(name = "id", in = ParameterIn.PATH, description = "ID do pedido", example = "11", required = true)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedido cancelado; a resposta traz o status CANCELADO",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PedidoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pedido não encontrado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.PEDIDO_NAO_ENCONTRADO)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Pedido fora de PENDENTE ou PAGO",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ErroResponseDTO.class),
                            examples = @ExampleObject(value = SwaggerExemplos.PEDIDO_CANCELAR_INVALIDO)
                    )
            )
    })
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<PedidoResponseDTO> cancelar(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(pedidoService.cancelar(id));
    }
}
