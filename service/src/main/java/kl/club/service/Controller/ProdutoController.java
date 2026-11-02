package kl.club.service.Controller;

import jakarta.validation.Valid;
import kl.club.service.Model.DTO.ProdutoDisponibilidadeRequestDTO;
import kl.club.service.Model.DTO.ProdutoRequestDTO;
import kl.club.service.Model.DTO.ProdutoResponseDTO;
import kl.club.service.Service.ProdutoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listarProdutos(
            @RequestParam(name = "categoria", required = false) Integer categoria,
            @RequestParam(name = "disponibilidade", required = false) Boolean disponibilidade) {
        return ResponseEntity.status(200).body(produtoService.listar(categoria, disponibilidade));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarProduto(@PathVariable("id") Integer id) {
        return ResponseEntity.status(200).body(produtoService.buscar(id));
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> cadastrarProduto(@RequestBody @Valid ProdutoRequestDTO requestDTO) {
        ProdutoResponseDTO produtoCadastrado = produtoService.cadastrar(requestDTO);
        return ResponseEntity.status(201).body(produtoCadastrado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizarProduto(
            @PathVariable("id") Integer id,
            @RequestBody @Valid ProdutoRequestDTO requestDTO) {
        return ResponseEntity.status(200).body(produtoService.atualizar(id, requestDTO));
    }

    @PatchMapping("/{id}/disponibilidade")
    public ResponseEntity<ProdutoResponseDTO> alterarDisponibilidade(
            @PathVariable("id") Integer id,
            @RequestBody @Valid ProdutoDisponibilidadeRequestDTO requestDTO) {
        return ResponseEntity.status(200).body(produtoService.alterarDisponibilidade(id, requestDTO));
    }
}
