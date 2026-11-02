package kl.club.service.Repository;

import kl.club.service.Model.Entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

    boolean existsByNome(String nome);
    boolean existsByNomeAndIdProdutoNot(String nome, Integer idProduto);

    @Query("""
            SELECT p FROM Produto p
            JOIN FETCH p.categoria c
            WHERE (:idCategoria IS NULL OR c.idCategoria = :idCategoria)
            AND (:disponibilidade IS NULL OR p.disponibilidade = :disponibilidade)
            """)
    List<Produto> listar(@Param("idCategoria") Integer idCategoria, @Param("disponibilidade") Boolean disponibilidade);
}
