package kl.club.service.Repository;

import kl.club.service.Model.Entity.Pedido;
import kl.club.service.Model.enums.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    @Query("""
        SELECT DISTINCT p
        FROM Pedido p
        LEFT JOIN FETCH p.itens i
        LEFT JOIN FETCH i.produto
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
}

