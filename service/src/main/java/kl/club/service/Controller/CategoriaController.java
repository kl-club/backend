package kl.club.service.Controller;

import jakarta.validation.Valid;
import kl.club.service.Model.DTO.CategoriaRequestDTO;
import kl.club.service.Model.DTO.CategoriaResponseDTO;
import kl.club.service.Service.CategoriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listarCategorias() {
        return ResponseEntity.status(200).body(categoriaService.listar());
    }

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> cadastrarCategoria(@RequestBody @Valid CategoriaRequestDTO requestDTO) {
        CategoriaResponseDTO categoriaCadastrada = categoriaService.cadastrar(requestDTO);
        return ResponseEntity.status(201).body(categoriaCadastrada);
    }
}
