package kl.club.service.Repository;

import kl.club.service.Model.Entity.Pedido;
import kl.club.service.Model.enums.FormaPagamento;
import kl.club.service.Model.enums.Modalidade;
import kl.club.service.Model.enums.StatusPedido;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    @Query("""
        SELECT DISTINCT p
        FROM Pedido p
        LEFT JOIN FETCH p.itens i
        LEFT JOIN FETCH i.produto produto
        LEFT JOIN FETCH produto.categoria
        WHERE p.idPedido IN :ids
        """)
    List<Pedido> buscarPedidosComItens(
            @Param("ids") List<Integer> ids
    );


    @Query("""
        SELECT COALESCE(SUM(p.pontosGerados), 0L)
        FROM Pedido p
        WHERE p.usuario.idUsuario = :idUsuario
        AND p.statusPedido NOT IN :statusExcluidos
        """)
    Long somarPontosPorUsuario(
            @Param("idUsuario") Integer idUsuario,
            @Param("statusExcluidos") List<StatusPedido> statusExcluidos
    );

    @Query("""
        SELECT DISTINCT p
        FROM Pedido p
        LEFT JOIN FETCH p.itens i
        LEFT JOIN FETCH i.produto produto
        LEFT JOIN FETCH produto.categoria
        WHERE p.usuario.idUsuario = :idUsuario
        ORDER BY p.dataHora DESC
        """)
    List<Pedido> buscarPedidosPorUsuario(
            @Param("idUsuario") Integer idUsuario
    );

    long countByUsuario_IdUsuario(Integer idUsuario);

    @Query("""
        SELECT SUM(p.valorTotal)
        FROM Pedido p
        WHERE p.usuario.idUsuario = :idUsuario
        AND p.statusPedido = :status
        """)
    BigDecimal somarValorGastoPorUsuario(
            @Param("idUsuario") Integer idUsuario,
            @Param("status") StatusPedido status
    );

    Optional<Pedido> findFirstByUsuario_IdUsuarioOrderByDataHoraDescIdPedidoDesc(
            Integer idUsuario
    );

    @Query("""
        SELECT DISTINCT p
        FROM Pedido p
        LEFT JOIN FETCH p.itens i
        LEFT JOIN FETCH i.produto produto
        LEFT JOIN FETCH produto.categoria
        WHERE p.idPedido = :pedidoId
        AND p.usuario.idUsuario = :usuarioId
        """)
    Optional<Pedido> buscarPedidoPorUsuario(
            @Param("usuarioId") Integer usuarioId,
            @Param("pedidoId") Integer pedidoId
    );

    // Pedido com cliente, itens e produtos em uma consulta só
    @Query("""
        SELECT DISTINCT p
        FROM Pedido p
        LEFT JOIN FETCH p.usuario
        LEFT JOIN FETCH p.itens i
        LEFT JOIN FETCH i.produto produto
        LEFT JOIN FETCH produto.categoria
        WHERE p.idPedido = :id
        """)
    Optional<Pedido> buscarPorIdComItens(@Param("id") Integer id);

    // Listas da gestão: pedidos do período nos status pedidos, em ordem de fila (mais antigos primeiro)
    @Query("""
        SELECT DISTINCT p
        FROM Pedido p
        LEFT JOIN FETCH p.usuario
        LEFT JOIN FETCH p.itens i
        LEFT JOIN FETCH i.produto produto
        LEFT JOIN FETCH produto.categoria
        WHERE p.statusPedido IN :status
        AND p.dataHora >= :inicio
        AND p.dataHora < :fim
        ORDER BY p.dataHora ASC, p.idPedido ASC
        """)
    List<Pedido> buscarPorStatusNoPeriodo(
            @Param("status") List<StatusPedido> status,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );

    /*
     * Busca com trava de escrita (SELECT ... FOR UPDATE) para avançar e cancelar.
     * Uma segunda requisição no mesmo pedido espera a primeira terminar e lê o status
     * já atualizado, em vez de sobrescrever a transição da outra.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Pedido p WHERE p.idPedido = :id")
    Optional<Pedido> buscarParaAtualizar(@Param("id") Integer id);

    /*
     * Histórico: filtros opcionais e combináveis (parâmetro null = sem filtro).
     * Os itens não entram aqui, porque paginar com JOIN FETCH de coleção faz o Hibernate
     * paginar em memória. Eles são carregados depois, com buscarPedidosComItens.
     * A ordenação vem do Pageable.
     */
    @Query(value = """
        SELECT p
        FROM Pedido p
        LEFT JOIN FETCH p.usuario u
        WHERE (:idPedido IS NULL OR p.idPedido = :idPedido)
        AND (:nomeUsuario IS NULL OR LOWER(u.nome) LIKE LOWER(CONCAT('%', :nomeUsuario, '%')))
        AND (:inicio IS NULL OR p.dataHora >= :inicio)
        AND (:fim IS NULL OR p.dataHora < :fim)
        AND (:modalidade IS NULL OR p.modalidade = :modalidade)
        """,
            countQuery = """
        SELECT COUNT(p)
        FROM Pedido p
        LEFT JOIN p.usuario u
        WHERE (:idPedido IS NULL OR p.idPedido = :idPedido)
        AND (:nomeUsuario IS NULL OR LOWER(u.nome) LIKE LOWER(CONCAT('%', :nomeUsuario, '%')))
        AND (:inicio IS NULL OR p.dataHora >= :inicio)
        AND (:fim IS NULL OR p.dataHora < :fim)
        AND (:modalidade IS NULL OR p.modalidade = :modalidade)
        """)
    Page<Pedido> buscarHistorico(
            @Param("idPedido") Integer idPedido,
            @Param("nomeUsuario") String nomeUsuario,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("modalidade") Modalidade modalidade,
            Pageable pageable
    );

    // ----- Dashboard: pedidos CANCELADO ficam fora de todas as agregações -----

    /** Totais de um período para os KPIs. Sem pedidos, faturamento e pedidosSemCliente vêm null. */
    interface ResumoPeriodo {
        Long getQuantidade();
        BigDecimal getFaturamento();
        Long getClientesCadastrados();
        Long getPedidosSemCliente();
    }

    @Query("""
        SELECT COUNT(p) AS quantidade,
               SUM(p.valorTotal) AS faturamento,
               COUNT(DISTINCT p.usuario.idUsuario) AS clientesCadastrados,
               SUM(CASE WHEN p.usuario IS NULL THEN 1 ELSE 0 END) AS pedidosSemCliente
        FROM Pedido p
        WHERE p.statusPedido <> kl.club.service.Model.enums.StatusPedido.CANCELADO
        AND p.dataHora >= :inicio
        AND p.dataHora < :fim
        """)
    ResumoPeriodo resumirPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    interface TotalFormaPagamento {
        FormaPagamento getFormaPagamento();
        BigDecimal getValorTotal();
    }

    @Query("""
        SELECT p.formaPagamento AS formaPagamento, SUM(p.valorTotal) AS valorTotal
        FROM Pedido p
        WHERE p.statusPedido <> kl.club.service.Model.enums.StatusPedido.CANCELADO
        AND p.dataHora >= :inicio
        AND p.dataHora < :fim
        GROUP BY p.formaPagamento
        ORDER BY SUM(p.valorTotal) DESC, p.formaPagamento ASC
        """)
    List<TotalFormaPagamento> somarPorFormaPagamento(@Param("inicio") LocalDateTime inicio,
                                                     @Param("fim") LocalDateTime fim);

    interface ProdutoVendido {
        Integer getIdProduto();
        String getNome();
        String getImagem();
        Long getQuantidadeVendida();
    }

    /** Ranking por unidades vendidas, com desempate pelo nome. O limite vem do Pageable. */
    @Query("""
        SELECT pr.idProduto AS idProduto, pr.nome AS nome, pr.imagem AS imagem,
               SUM(i.quantidade) AS quantidadeVendida
        FROM ItemPedido i
        JOIN i.pedido p
        JOIN i.produto pr
        WHERE p.statusPedido <> kl.club.service.Model.enums.StatusPedido.CANCELADO
        AND p.dataHora >= :inicio
        AND p.dataHora < :fim
        GROUP BY pr.idProduto, pr.nome, pr.imagem
        ORDER BY SUM(i.quantidade) DESC, pr.nome ASC
        """)
    List<ProdutoVendido> buscarMaisVendidos(@Param("inicio") LocalDateTime inicio,
                                            @Param("fim") LocalDateTime fim,
                                            Pageable pageable);
}

