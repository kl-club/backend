package kl.club.service.Repository;

import kl.club.service.Model.Entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Integer> {

    boolean existsByEmail(String email);
    boolean existsByEmailAndIdFuncionarioNot(String email, Integer idFuncionario);
    Optional<Funcionario> findByEmail(String email);
}
