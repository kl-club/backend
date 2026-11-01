package kl.club.service.Service;

import kl.club.service.Exception.CategoriaNomeExistenteException;
import kl.club.service.Model.DTO.CategoriaRequestDTO;
import kl.club.service.Model.DTO.CategoriaResponseDTO;
import kl.club.service.Model.Entity.Categoria;
import kl.club.service.Model.Mapper.CategoriaMapper;
import kl.club.service.Repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<CategoriaResponseDTO> listar() {
        return CategoriaMapper.toResponse(categoriaRepository.findAll());
    }

    public CategoriaResponseDTO cadastrar(CategoriaRequestDTO requestDTO) {

        if (categoriaRepository.existsByNome(requestDTO.nome())) {
            throw new CategoriaNomeExistenteException();
        }

        Categoria categoriaCadastrar = CategoriaMapper.toEntity(requestDTO);

        return CategoriaMapper.toResponse(categoriaRepository.save(categoriaCadastrar));
    }
}
