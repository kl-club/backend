package kl.club.service.Repository;

import kl.club.service.Model.Entity.Pedido;
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
}
