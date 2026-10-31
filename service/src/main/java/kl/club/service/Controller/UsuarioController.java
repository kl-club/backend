package kl.club.service.Controller;

import jakarta.validation.Valid;
import kl.club.service.Model.DTO.UsuarioAutenticacaoRequestDTO;
import kl.club.service.Model.DTO.UsuarioRequestDTO;
import kl.club.service.Model.DTO.UsuarioResponseDTO;
import kl.club.service.Repository.UsuarioRepository;
import kl.club.service.Service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrarUsuario(@RequestBody @Valid UsuarioRequestDTO requestDTO) {
        UsuarioResponseDTO usuarioCadastrado = usuarioService.cadastrar(requestDTO);
        return ResponseEntity.status(201).body(usuarioCadastrado);
    }

    // Fazer um endpoint para após o cara inserir o valor no campo do frontend,
    // o próprio frontend enviar uma chamada pra api verificar se já existe algum registro com aquele valor
    //@GetMapping()
    //public ResponseEntity<UsuarioResponseDTO> existsByCpfOuTelefone(
    //      @RequestParam("cpf") String cpf,
    //      @RequestParam("telefone") String telefone){
    //
    // }
}
