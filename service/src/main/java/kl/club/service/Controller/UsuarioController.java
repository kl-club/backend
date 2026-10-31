package kl.club.service.Controller;

import jakarta.validation.Valid;
import kl.club.service.Model.DTO.UsuarioAutenticationRequestDTO;
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
    public ResponseEntity<UsuarioResponseDTO> cadastrarUsuario(@RequestBody @Valid UsuarioRequestDTO requestDTO){
        UsuarioResponseDTO usuarioCadastrar =  usuarioService.cadastrar(requestDTO);
        return ResponseEntity.status(201).body(usuarioCadastrar);
    }




}
