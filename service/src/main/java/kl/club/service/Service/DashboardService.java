package kl.club.service.Service;

import kl.club.service.Model.DTO.DashboardKpisResponseDTO;
import kl.club.service.Model.DTO.DashboardPagamentosResponseDTO;
import kl.club.service.Model.DTO.DashboardProdutosResponseDTO;
import kl.club.service.Model.DTO.FormaPagamentoResumoResponseDTO;
import kl.club.service.Model.DTO.PaginaResponseDTO;
import kl.club.service.Model.DTO.PedidoDashboardResponseDTO;
import kl.club.service.Model.DTO.ProdutoVendidoResponseDTO;
import kl.club.service.Model.Entity.Pedido;
import kl.club.service.Model.Mapper.PedidoMapper;
import kl.club.service.Model.enums.PeriodoDashboard;
import kl.club.service.Repository.PedidoRepository;
import kl.club.service.Repository.PedidoRepository.ProdutoVendido;
import kl.club.service.Repository.PedidoRepository.ResumoPeriodo;
import kl.club.service.Repository.PedidoRepository.TotalFormaPagamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Números do dashboard. Pedidos CANCELADO ficam fora dos KPIs, da distribuição de pagamentos
 * e do ranking de produtos, mas aparecem na tabela de pedidos. Pedidos PENDENTE entram em tudo.
 */
@Service
public class DashboardService {

    private static final int TAMANHO_RANKING = 5;
    private static final BigDecimal CEM = BigDecimal.valueOf(100);

    private final PedidoRepository pedidoRepository;

    public DashboardService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional(readOnly = true)
    public DashboardKpisResponseDTO calcularKpis(PeriodoDashboard periodo) {

        Intervalo atual = intervalo(periodo);
        // Período anterior: o dia de ontem inteiro, mesmo com hoje ainda em andamento
        Intervalo anterior = new Intervalo(atual.inicio().minusDays(1), atual.inicio());

        ResumoPeriodo resumoAtual = pedidoRepository.resumirPeriodo(atual.inicio(), atual.fim());
        ResumoPeriodo resumoAnterior = pedidoRepository.resumirPeriodo(anterior.inicio(), anterior.fim());

        long quantidade = resumoAtual.getQuantidade();
        long quantidadeAnterior = resumoAnterior.getQuantidade();
        BigDecimal faturamento = valorOuZero(resumoAtual.getFaturamento());

        BigDecimal variacao = quantidadeAnterior == 0
                ? null
                : BigDecimal.valueOf(quantidade - quantidadeAnterior)
                        .multiply(CEM)
                        .divide(BigDecimal.valueOf(quantidadeAnterior), 1, RoundingMode.HALF_UP);

        long pedidosSemCliente = resumoAtual.getPedidosSemCliente() == null ? 0 : resumoAtual.getPedidosSemCliente();
        // Cada cliente cadastrado conta uma vez; cada pedido sem cliente conta 1
        long clientesAtendidos = resumoAtual.getClientesCadastrados() + pedidosSemCliente;

        return new DashboardKpisResponseDTO(
                periodo,
                faturamento,
                quantidade,
                variacao,
                ticketMedio(faturamento, quantidade),
                ticketMedio(valorOuZero(resumoAnterior.getFaturamento()), quantidadeAnterior),
                clientesAtendidos
        );
    }

    /** Todos os pedidos do período, inclusive cancelados, dos mais recentes para os mais antigos. */
    @Transactional(readOnly = true)
    public PaginaResponseDTO<PedidoDashboardResponseDTO> listarPedidos(PeriodoDashboard periodo,
                                                                       Integer pagina, Integer tamanho) {

        PedidoService.validarPaginacao(pagina, tamanho);

        Intervalo atual = intervalo(periodo);
        Sort maisRecentesPrimeiro = Sort.by(Sort.Direction.DESC, "dataHora", "idPedido");

        // Reaproveita a consulta do histórico, só com o filtro de data
        Page<Pedido> paginaPedidos = pedidoRepository.buscarHistorico(null, null, atual.inicio(), atual.fim(), null,
                PageRequest.of(pagina - 1, tamanho, maisRecentesPrimeiro));

        List<Integer> idsPedidos = paginaPedidos.getContent().stream().map(Pedido::getIdPedido).toList();
        if (!idsPedidos.isEmpty()) {
            pedidoRepository.buscarPedidosComItens(idsPedidos);
        }

        List<PedidoDashboardResponseDTO> conteudo = paginaPedidos.getContent()
                .stream()
                .map(PedidoMapper::toDashboardResponse)
                .toList();

        return new PaginaResponseDTO<>(conteudo, pagina, tamanho,
                paginaPedidos.getTotalElements(), paginaPedidos.getTotalPages());
    }

    /** Valor e percentual (sobre o valor, não sobre a quantidade) de cada forma de pagamento usada. */
    @Transactional(readOnly = true)
    public DashboardPagamentosResponseDTO calcularPagamentos(PeriodoDashboard periodo) {

        Intervalo atual = intervalo(periodo);
        List<TotalFormaPagamento> totais = pedidoRepository.somarPorFormaPagamento(atual.inicio(), atual.fim());

        BigDecimal faturamento = totais.stream()
                .map(TotalFormaPagamento::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        List<FormaPagamentoResumoResponseDTO> formas = totais.stream()
                .map(total -> new FormaPagamentoResumoResponseDTO(
                        total.getFormaPagamento(),
                        total.getValorTotal(),
                        total.getValorTotal().multiply(CEM).divide(faturamento, 1, RoundingMode.HALF_UP)))
                .toList();

        return new DashboardPagamentosResponseDTO(periodo, faturamento, formas);
    }

    /** Os 5 produtos com mais unidades vendidas no período, com desempate pelo nome. */
    @Transactional(readOnly = true)
    public DashboardProdutosResponseDTO listarMaisVendidos(PeriodoDashboard periodo) {

        Intervalo atual = intervalo(periodo);
        List<ProdutoVendido> ranking = pedidoRepository.buscarMaisVendidos(atual.inicio(), atual.fim(),
                PageRequest.of(0, TAMANHO_RANKING));

        List<ProdutoVendidoResponseDTO> produtos = new ArrayList<>();
        for (int i = 0; i < ranking.size(); i++) {
            ProdutoVendido produto = ranking.get(i);
            produtos.add(new ProdutoVendidoResponseDTO(
                    i + 1,
                    produto.getIdProduto(),
                    produto.getNome(),
                    produto.getImagem(),
                    produto.getQuantidadeVendida()
            ));
        }

        return new DashboardProdutosResponseDTO(periodo, produtos);
    }

    private Intervalo intervalo(PeriodoDashboard periodo) {
        return switch (periodo) {
            case HOJE -> {
                LocalDateTime inicioDoDia = LocalDate.now(PedidoService.FUSO).atStartOfDay();
                yield new Intervalo(inicioDoDia, inicioDoDia.plusDays(1));
            }
        };
    }

    private static BigDecimal ticketMedio(BigDecimal faturamento, long quantidade) {
        return quantidade == 0 ? null : faturamento.divide(BigDecimal.valueOf(quantidade), 2, RoundingMode.HALF_UP);
    }

    private static BigDecimal valorOuZero(BigDecimal valor) {
        return valor == null ? BigDecimal.ZERO.setScale(2) : valor;
    }

    /** Intervalo [inicio, fim) no horário de São Paulo. */
    private record Intervalo(LocalDateTime inicio, LocalDateTime fim) {
    }
}
