package kl.club.service.Service;

import kl.club.service.Exception.ParametroInvalidoException;
import kl.club.service.Exception.PedidoNaoEncontradoException;
import kl.club.service.Exception.ProdutoIndisponivelException;
import kl.club.service.Exception.ProdutoNaoEncontradoException;
import kl.club.service.Exception.ProdutoRepetidoException;
import kl.club.service.Exception.TransicaoStatusInvalidaException;
import kl.club.service.Exception.UsuarioNaoEncontradoException;
import kl.club.service.Model.DTO.HistoricoFiltroDTO;
import kl.club.service.Model.DTO.ItemPedidoRequestDTO;
import kl.club.service.Model.DTO.PaginaResponseDTO;
import kl.club.service.Model.DTO.PedidoHistoricoResponseDTO;
import kl.club.service.Model.DTO.PedidoRequestDTO;
import kl.club.service.Model.DTO.PedidoResponseDTO;
import kl.club.service.Model.Entity.ItemPedido;
import kl.club.service.Model.Entity.Pedido;
import kl.club.service.Model.Entity.Produto;
import kl.club.service.Model.Entity.Usuario;
import kl.club.service.Model.Mapper.ItemPedidoMapper;
import kl.club.service.Model.Mapper.PedidoMapper;
import kl.club.service.Model.enums.StatusPedido;
import kl.club.service.Repository.PedidoRepository;
import kl.club.service.Repository.ProdutoRepository;
import kl.club.service.Repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class PedidoService {

    /** O "dia do pedido" é sempre o de São Paulo, independente do fuso do servidor. */
    static final ZoneId FUSO = ZoneId.of("America/Sao_Paulo");

    private static final int TAMANHO_MAXIMO_PAGINA = 50;

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;

    public PedidoService(PedidoRepository pedidoRepository, ProdutoRepository produtoRepository,
                         UsuarioRepository usuarioRepository) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Cria o pedido e os itens em uma transação. Preço, total, pontos, status e data
     * são sempre calculados aqui, nunca recebidos do cliente.
     */
    @Transactional
    public PedidoResponseDTO criar(PedidoRequestDTO requestDTO) {

        Usuario usuario = buscarUsuario(requestDTO.idUsuario());

        validarProdutosRepetidos(requestDTO.itens());

        Map<Integer, Produto> produtos = buscarProdutosDisponiveis(requestDTO.itens());

        Pedido pedido = PedidoMapper.toEntity(requestDTO);
        pedido.setUsuario(usuario);
        pedido.setStatusPedido(requestDTO.formaPagamento().statusInicial());
        pedido.setDataHora(LocalDateTime.now(FUSO).truncatedTo(ChronoUnit.SECONDS));

        BigDecimal valorTotal = BigDecimal.ZERO;
        int pontos = 0;

        for (ItemPedidoRequestDTO itemDTO : requestDTO.itens()) {
            ItemPedido item = ItemPedidoMapper.toEntity(produtos.get(itemDTO.idProduto()), itemDTO.quantidade());
            pedido.adicionarItem(item);

            valorTotal = valorTotal.add(item.calcularValorTotal());
            pontos += item.calcularPontos();
        }

        pedido.setValorTotal(valorTotal.setScale(2, RoundingMode.HALF_UP));
        // Pedido sem cliente não gera pontos
        pedido.setPontosGerados(usuario == null ? 0 : pontos);

        return PedidoMapper.toResponse(pedidoRepository.save(pedido));
    }

    @Transactional(readOnly = true)
    public PedidoResponseDTO buscarPorId(Integer id) {

        Pedido pedido = pedidoRepository.buscarPorIdComItens(id)
                .orElseThrow(PedidoNaoEncontradoException::new);

        return PedidoMapper.toResponse(pedido);
    }

    /** Lista da gestão: pedidos de hoje nos status informados, mais antigos primeiro. */
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarDoDia(List<StatusPedido> status) {

        LocalDateTime inicioDoDia = LocalDate.now(FUSO).atStartOfDay();

        return pedidoRepository.buscarPorStatusNoPeriodo(status, inicioDoDia, inicioDoDia.plusDays(1))
                .stream()
                .map(PedidoMapper::toResponse)
                .toList();
    }

    /** Move o pedido uma etapa adiante. O próximo status é definido pelo back-end. */
    @Transactional
    public PedidoResponseDTO avancar(Integer id) {

        Pedido pedido = buscarParaAtualizar(id);
        StatusPedido statusAtual = pedido.getStatusPedido();

        StatusPedido proximoStatus = statusAtual.proximo()
                .orElseThrow(() -> new TransicaoStatusInvalidaException(statusAtual == StatusPedido.CANCELADO
                        ? "O pedido #" + id + " foi cancelado e não pode avançar"
                        : "O pedido #" + id + " já foi concluído e não pode avançar"));

        pedido.setStatusPedido(proximoStatus);

        return PedidoMapper.toResponse(pedido);
    }

    /**
     * Cancela um pedido PENDENTE ou PAGO. Os pontos deixam de contar no saldo do cliente,
     * mas pontos_gerados não muda. Sem estorno: ainda não há integração de pagamentos.
     */
    @Transactional
    public PedidoResponseDTO cancelar(Integer id) {

        Pedido pedido = buscarParaAtualizar(id);
        StatusPedido statusAtual = pedido.getStatusPedido();

        if (!statusAtual.podeSerCancelado()) {
            throw new TransicaoStatusInvalidaException(statusAtual == StatusPedido.CANCELADO
                    ? "O pedido #" + id + " já foi cancelado"
                    : "O pedido #" + id + " está " + statusAtual
                    + " e não pode ser cancelado. Só pedidos PENDENTE ou PAGO podem ser cancelados");
        }

        pedido.setStatusPedido(StatusPedido.CANCELADO);

        return PedidoMapper.toResponse(pedido);
    }

    /**
     * Histórico paginado com filtros opcionais e combináveis, incluindo pedidos de todos os status.
     * Sem ordenarPor: mais recentes primeiro. Com ordenarPor=valor: pelo valor total, com desempate
     * pelo mais recente.
     */
    @Transactional(readOnly = true)
    public PaginaResponseDTO<PedidoHistoricoResponseDTO> listarHistorico(HistoricoFiltroDTO filtro) {

        validarFiltroHistorico(filtro);

        Sort maisRecentesPrimeiro = Sort.by(Sort.Direction.DESC, "dataHora", "idPedido");
        Sort ordenacao = maisRecentesPrimeiro;

        if (filtro.ordenarPor() != null) {
            Sort.Direction direcao = "asc".equals(filtro.direcao()) ? Sort.Direction.ASC : Sort.Direction.DESC;
            ordenacao = Sort.by(direcao, "valorTotal").and(maisRecentesPrimeiro);
        }

        String nomeUsuario = filtro.nomeUsuario() == null || filtro.nomeUsuario().isBlank()
                ? null
                : filtro.nomeUsuario().trim();
        LocalDateTime inicio = filtro.dataInicio() == null ? null : filtro.dataInicio().atStartOfDay();
        // dataFim é inclusiva: vai até o início do dia seguinte
        LocalDateTime fim = filtro.dataFim() == null ? null : filtro.dataFim().plusDays(1).atStartOfDay();

        Page<Pedido> pagina = pedidoRepository.buscarHistorico(
                filtro.idPedido(), nomeUsuario, inicio, fim, filtro.modalidade(),
                PageRequest.of(filtro.pagina() - 1, filtro.tamanho(), ordenacao));

        // Carrega itens e produtos dos pedidos da página em uma consulta só
        List<Integer> idsPedidos = pagina.getContent().stream().map(Pedido::getIdPedido).toList();
        if (!idsPedidos.isEmpty()) {
            pedidoRepository.buscarPedidosComItens(idsPedidos);
        }

        List<PedidoHistoricoResponseDTO> conteudo = pagina.getContent()
                .stream()
                .map(PedidoMapper::toHistoricoResponse)
                .toList();

        return new PaginaResponseDTO<>(conteudo, filtro.pagina(), filtro.tamanho(),
                pagina.getTotalElements(), pagina.getTotalPages());
    }

    /** Regras de paginação da API: pagina a partir de 1 e tamanho de 1 a 50. */
    static void validarPaginacao(Integer pagina, Integer tamanho) {

        if (pagina < 1) {
            throw new ParametroInvalidoException("A página deve ser no mínimo 1");
        }

        if (tamanho < 1 || tamanho > TAMANHO_MAXIMO_PAGINA) {
            throw new ParametroInvalidoException("O tamanho deve ser de 1 a " + TAMANHO_MAXIMO_PAGINA);
        }
    }

    private void validarFiltroHistorico(HistoricoFiltroDTO filtro) {

        validarPaginacao(filtro.pagina(), filtro.tamanho());

        if (filtro.dataInicio() != null && filtro.dataFim() != null && filtro.dataInicio().isAfter(filtro.dataFim())) {
            throw new ParametroInvalidoException("A data inicial não pode ser depois da data final");
        }

        if (filtro.ordenarPor() != null && !"valor".equals(filtro.ordenarPor())) {
            throw new ParametroInvalidoException("O ordenarPor só aceita: valor");
        }

        if (filtro.direcao() != null && !"asc".equals(filtro.direcao()) && !"desc".equals(filtro.direcao())) {
            throw new ParametroInvalidoException("A direcao só aceita: asc, desc");
        }
    }

    private Pedido buscarParaAtualizar(Integer id) {
        return pedidoRepository.buscarParaAtualizar(id)
                .orElseThrow(PedidoNaoEncontradoException::new);
    }

    private Usuario buscarUsuario(Integer idUsuario) {

        if (idUsuario == null) {
            return null;
        }

        return usuarioRepository.findById(idUsuario)
                .orElseThrow(UsuarioNaoEncontradoException::new);
    }

    /** Cada produto deve aparecer uma única vez, com a quantidade desejada. */
    private void validarProdutosRepetidos(List<ItemPedidoRequestDTO> itens) {

        Set<Integer> idsProdutos = new HashSet<>();

        for (ItemPedidoRequestDTO item : itens) {
            if (!idsProdutos.add(item.idProduto())) {
                throw new ProdutoRepetidoException(item.idProduto());
            }
        }
    }

    /** Busca todos os produtos de uma vez e garante que existem e estão disponíveis. */
    private Map<Integer, Produto> buscarProdutosDisponiveis(List<ItemPedidoRequestDTO> itens) {

        List<Integer> idsProdutos = itens.stream()
                .map(ItemPedidoRequestDTO::idProduto)
                .toList();

        Map<Integer, Produto> produtos = new HashMap<>();
        for (Produto produto : produtoRepository.findAllById(idsProdutos)) {
            produtos.put(produto.getIdProduto(), produto);
        }

        for (Integer idProduto : idsProdutos) {
            Produto produto = produtos.get(idProduto);

            if (produto == null) {
                throw new ProdutoNaoEncontradoException(idProduto);
            }

            // A categoria não é verificada: produto disponível de categoria inativa pode ser pedido
            if (!Boolean.TRUE.equals(produto.getDisponibilidade())) {
                throw new ProdutoIndisponivelException(produto.getNome());
            }
        }

        return produtos;
    }
}
