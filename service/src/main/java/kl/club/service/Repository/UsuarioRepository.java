package kl.club.service.Repository;

import kl.club.service.Model.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    boolean existsByCpf(String cpf);
    boolean existsByTelefone(String telefone);
    Optional<Usuario> findByCpf(String cpf);

    // Essa consulta carrega todos os usuários e seus pedidos incluindo usuarios sem pedidos.
    @Query("""
        SELECT DISTINCT u
        FROM Usuario u
        LEFT JOIN FETCH u.pedidos
        """)
    List<Usuario> buscarTodosComPedidos();

    @Query("""
        SELECT DISTINCT u
        FROM Usuario u
        LEFT JOIN FETCH u.pedidos
        WHERE u.idUsuario = :id
        """)
    Optional<Usuario> buscarPorIdComPedidos(@Param("id") Integer id);
}
