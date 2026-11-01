package kl.club.service.Repository;

import kl.club.service.Model.Entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    boolean existsByNome(String nome);
}
