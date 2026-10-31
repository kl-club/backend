package kl.club.service.repository;

import kl.club.service.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    boolean existsByCpf(String cpf);
    boolean existsByTelefone(String telefone);
}
