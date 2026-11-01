package kl.club.service.Controller;

import jakarta.validation.Valid;
import kl.club.service.Model.DTO.FuncionarioAtualizacaoRequestDTO;
import kl.club.service.Model.DTO.FuncionarioLoginRequestDTO;
import kl.club.service.Model.DTO.FuncionarioRequestDTO;
import kl.club.service.Model.DTO.FuncionarioResponseDTO;
import kl.club.service.Model.DTO.FuncionarioSenhaRequestDTO;
import kl.club.service.Service.FuncionarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @GetMapping
    public ResponseEntity<List<FuncionarioResponseDTO>> listarFuncionarios() {
        return ResponseEntity.status(200).body(funcionarioService.listar());
    }

    @PostMapping
    public ResponseEntity<FuncionarioResponseDTO> cadastrarFuncionario(@RequestBody @Valid FuncionarioRequestDTO requestDTO) {
        FuncionarioResponseDTO funcionarioCadastrado = funcionarioService.cadastrar(requestDTO);
        return ResponseEntity.status(201).body(funcionarioCadastrado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDTO> atualizarFuncionario(
            @PathVariable Integer id,
            @RequestBody @Valid FuncionarioAtualizacaoRequestDTO requestDTO) {
        return ResponseEntity.status(200).body(funcionarioService.atualizar(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativarFuncionario(@PathVariable Integer id) {
        funcionarioService.desativar(id);
        return ResponseEntity.status(204).build();
    }

    @PatchMapping("/{id}/senha")
    public ResponseEntity<Void> trocarSenha(
            @PathVariable Integer id,
            @RequestBody @Valid FuncionarioSenhaRequestDTO requestDTO) {
        funcionarioService.trocarSenha(id, requestDTO);
        return ResponseEntity.status(204).build();
    }

    @PostMapping("/login")
    public ResponseEntity<FuncionarioResponseDTO> login(@RequestBody @Valid FuncionarioLoginRequestDTO requestDTO) {
        return ResponseEntity.status(200).body(funcionarioService.login(requestDTO));
    }
}
