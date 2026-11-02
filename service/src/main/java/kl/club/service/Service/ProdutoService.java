package kl.club.service.Service;

import kl.club.service.Exception.CategoriaNaoEncontradaException;
import kl.club.service.Exception.ProdutoNaoEncontradoException;
import kl.club.service.Exception.ProdutoNomeExistenteException;
import kl.club.service.Model.DTO.ProdutoDisponibilidadeRequestDTO;
import kl.club.service.Model.DTO.ProdutoRequestDTO;
import kl.club.service.Model.DTO.ProdutoResponseDTO;
import kl.club.service.Model.Entity.Categoria;
import kl.club.service.Model.Entity.Produto;
import kl.club.service.Model.Mapper.ProdutoMapper;
import kl.club.service.Repository.CategoriaRepository;
import kl.club.service.Repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoService(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<ProdutoResponseDTO> listar(Integer categoria, Boolean disponibilidade) {
        return ProdutoMapper.toResponse(produtoRepository.listar(categoria, disponibilidade));
    }

    public ProdutoResponseDTO buscar(Integer id) {
        return ProdutoMapper.toResponse(buscarPorId(id));
    }

    public ProdutoResponseDTO cadastrar(ProdutoRequestDTO requestDTO) {

        Categoria categoria = buscarCategoria(requestDTO.idCategoria());

        if (produtoRepository.existsByNome(requestDTO.nome())) {
            throw new ProdutoNomeExistenteException();
        }

        Produto produtoCadastrar = ProdutoMapper.toEntity(requestDTO, categoria);

        return ProdutoMapper.toResponse(produtoRepository.save(produtoCadastrar));
    }

    public ProdutoResponseDTO atualizar(Integer id, ProdutoRequestDTO requestDTO) {

        Produto produto = buscarPorId(id);
        Categoria categoria = buscarCategoria(requestDTO.idCategoria());

        if (produtoRepository.existsByNomeAndIdProdutoNot(requestDTO.nome(), id)) {
            throw new ProdutoNomeExistenteException();
        }

        produto.setCategoria(categoria);
        produto.setNome(requestDTO.nome());
        produto.setDescricao(requestDTO.descricao());
        produto.setIngredientes(requestDTO.ingredientes());
        produto.setPreco(requestDTO.preco());
        produto.setPontuacao(requestDTO.pontuacao());
        produto.setImagem(requestDTO.imagem());

        return ProdutoMapper.toResponse(produtoRepository.save(produto));
    }

    public ProdutoResponseDTO alterarDisponibilidade(Integer id, ProdutoDisponibilidadeRequestDTO requestDTO) {

        Produto produto = buscarPorId(id);

        produto.setDisponibilidade(requestDTO.disponibilidade());

        return ProdutoMapper.toResponse(produtoRepository.save(produto));
    }

    private Produto buscarPorId(Integer id) {
        return produtoRepository.findById(id)
                .orElseThrow(ProdutoNaoEncontradoException::new);
    }

    private Categoria buscarCategoria(Integer id) {
        return categoriaRepository.findById(id)
                .orElseThrow(CategoriaNaoEncontradaException::new);
    }
}
