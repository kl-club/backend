package kl.club.service.Service;

import kl.club.service.Exception.CategoriaNaoEncontradaException;
import kl.club.service.Exception.CategoriaNomeExistenteException;
import kl.club.service.Model.DTO.CategoriaAtivoRequestDTO;
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

    public CategoriaResponseDTO atualizar(Integer id, CategoriaRequestDTO requestDTO) {

        Categoria categoria = buscarPorId(id);

        if (categoriaRepository.existsByNomeAndIdCategoriaNot(requestDTO.nome(), id)) {
            throw new CategoriaNomeExistenteException();
        }

        categoria.setNome(requestDTO.nome());

        return CategoriaMapper.toResponse(categoriaRepository.save(categoria));
    }

    public CategoriaResponseDTO alterarAtivo(Integer id, CategoriaAtivoRequestDTO requestDTO) {

        Categoria categoria = buscarPorId(id);

        categoria.setAtivo(requestDTO.ativo());

        return CategoriaMapper.toResponse(categoriaRepository.save(categoria));
    }

    private Categoria buscarPorId(Integer id) {
        return categoriaRepository.findById(id)
                .orElseThrow(CategoriaNaoEncontradaException::new);
    }
}
