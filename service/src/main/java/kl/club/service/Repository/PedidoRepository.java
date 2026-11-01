package kl.club.service.Repository;

import kl.club.service.Model.Entity.Pedido;
import kl.club.service.Model.enums.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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
        AND p.statusPedido = :status
        """)
    Long somarPontosPorUsuario(
            @Param("idUsuario") Integer idUsuario,
            @Param("status") StatusPedido status
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
}
