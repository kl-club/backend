package kl.club.service.Controller;

import jakarta.validation.Valid;
import kl.club.service.Model.DTO.*;
import kl.club.service.Service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/verificar")
    public ResponseEntity<UsuarioVerificacaoResponseDTO> existsByCpfOuTelefone(
          @RequestParam("cpf") String cpf,
         @RequestParam("telefone") String telefone){


        UsuarioVerificacaoResponseDTO response =
                usuarioService.verificar(cpf, telefone);

        return ResponseEntity.status(200).body(response);

    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioResponseDTO> login(@RequestBody @Valid UsuarioAutenticacaoRequestDTO requestDTO){

        return ResponseEntity.status(200).body(usuarioService.login(requestDTO));
    }


    @GetMapping
    public ResponseEntity<List<UsuarioPedidosResponseDTO>> listarTodosComPedidos() {

        List<UsuarioPedidosResponseDTO> usuarios =
                usuarioService.listarTodosComPedidos();

        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioPedidosResponseDTO> buscarPorId(
            @PathVariable Integer id
    ) {

        UsuarioPedidosResponseDTO usuario =
                usuarioService.buscarPorId(id);

        return ResponseEntity.ok(usuario);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid UsuarioAtualizacaoRequestDTO dto
    ) {

        UsuarioResponseDTO usuarioAtualizado =
                usuarioService.atualizar(id, dto);

        return ResponseEntity.status(200).body(usuarioAtualizado);
    }


    @GetMapping("/{id}/pontos")
    public ResponseEntity<UsuarioPontosResponseDTO> consultarPontos(
            @PathVariable Integer id
    ) {

        UsuarioPontosResponseDTO response =
                usuarioService.consultarPontos(id);

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/{id}/pedidos")
    public ResponseEntity<List<PedidoDetalhadoResponseDTO>> listarPedidosPorUsuario(
            @PathVariable Integer id
    ) {

        List<PedidoDetalhadoResponseDTO> pedidos =
                usuarioService.listarPedidosPorUsuario(id);

        return ResponseEntity.status(200).body(pedidos);
    }

    @GetMapping("/{id}/resumo")
    public ResponseEntity<UsuarioResumoResponseDTO> buscarResumo(
            @PathVariable Integer id
    ) {

        UsuarioResumoResponseDTO resumo =
                usuarioService.buscarResumo(id);

        return ResponseEntity.ok(resumo);
    }
}
