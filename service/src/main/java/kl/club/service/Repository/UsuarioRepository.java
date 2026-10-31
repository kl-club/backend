package kl.club.service.Repository;

import kl.club.service.Model.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    boolean existsByCpf(String cpf);
    boolean existsByTelefone(String telefone);
}
