package kl.club.service.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    @GetMapping("/gestao/novos")
    public ResponseEntity<List<PedidoResponseDTO>> listarNovos() {
        return ResponseEntity.status(200).body(
                pedidoService.listarDoDia(List.of(StatusPedido.PENDENTE, StatusPedido.PAGO)));
    }

    @Operation(
            summary = "Gestão: em preparo",
            description = "Pedidos de hoje em EM_PREPARO, mais antigos primeiro."
    )
    @GetMapping("/gestao/em-preparo")
    public ResponseEntity<List<PedidoResponseDTO>> listarEmPreparo() {
        return ResponseEntity.status(200).body(pedidoService.listarDoDia(List.of(StatusPedido.EM_PREPARO)));
    }

    @Operation(
            summary = "Gestão: prontos para retirada",
            description = "Pedidos de hoje em PRONTO_PARA_RETIRADA, mais antigos primeiro."
    )
    @GetMapping("/gestao/prontos")
    public ResponseEntity<List<PedidoResponseDTO>> listarProntos() {
        return ResponseEntity.status(200).body(pedidoService.listarDoDia(List.of(StatusPedido.PRONTO_PARA_RETIRADA)));
    }

    @Operation(
            summary = "Gestão: concluídos",
            description = "Pedidos de hoje em CONCLUIDO, mais antigos primeiro."
    )
    @GetMapping("/gestao/concluidos")
    public ResponseEntity<List<PedidoResponseDTO>> listarConcluidos() {
        return ResponseEntity.status(200).body(pedidoService.listarDoDia(List.of(StatusPedido.CONCLUIDO)));
    }

    @Operation(
            summary = "Avançar status",
            description = """
                    Move o pedido uma etapa adiante. Não recebe corpo: o back-end define o próximo status.

                    PENDENTE ou PAGO → EM_PREPARO → PRONTO_PARA_RETIRADA → CONCLUIDO.
                    Pedido CONCLUIDO ou CANCELADO retorna 409 TRANSICAO_INVALIDA.
                    """
    )
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
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<PedidoResponseDTO> cancelar(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(pedidoService.cancelar(id));
    }
}
