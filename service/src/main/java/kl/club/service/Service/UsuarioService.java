package kl.club.service.Service;

import kl.club.service.Exception.UsuarioCpfExistenteException;
import kl.club.service.Exception.UsuarioNaoEncontradoException;
import kl.club.service.Exception.UsuarioTelefoneExistenteException;
import kl.club.service.Model.DTO.UsuarioAutenticacaoRequestDTO;
import kl.club.service.Model.DTO.UsuarioRequestDTO;
import kl.club.service.Model.DTO.UsuarioResponseDTO;
import kl.club.service.Model.DTO.UsuarioVerificacaoResponseDTO;
import kl.club.service.Model.Entity.Usuario;
import kl.club.service.Model.Mapper.UsuarioMapper;
import kl.club.service.Repository.PedidoRepository;
import kl.club.service.Repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PedidoRepository pedidoRepository;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PedidoRepository pedidoRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO requestDTO) {

        if (usuarioRepository.existsByCpf(requestDTO.cpf())) {
            throw new UsuarioCpfExistenteException();
        }

        if (usuarioRepository.existsByTelefone(requestDTO.telefone())) {
            throw new UsuarioTelefoneExistenteException();
        }

        Usuario usuarioCadastrar = UsuarioMapper.toEntity(requestDTO);

        return UsuarioMapper.toResponse(usuarioRepository.save(usuarioCadastrar));
    }


    public UsuarioVerificacaoResponseDTO verificar(
            String cpf,
            String telefone
    ) {
        if ((cpf == null || cpf.isBlank()) &&
                (telefone == null || telefone.isBlank())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Informe um CPF ou telefone"
            );
        }

        Boolean cpfEmUso = null;
        Boolean telefoneEmUso = null;

        if (cpf != null && !cpf.isBlank()) {
            cpfEmUso = usuarioRepository.existsByCpf(cpf);
        }

        if (telefone != null && !telefone.isBlank()) {
            telefoneEmUso = usuarioRepository.existsByTelefone(telefone);
        }

        return new UsuarioVerificacaoResponseDTO(
                cpfEmUso,
                telefoneEmUso
        );
    }

    public UsuarioResponseDTO login(UsuarioAutenticacaoRequestDTO requestDTO){

        Optional<Usuario> usuario = usuarioRepository.findByCpf(requestDTO.cpf());

        if (usuario.isEmpty()){
            throw new UsuarioNaoEncontradoException("");
        }

        return UsuarioMapper.toResponse(usuario.get());
    }
}
