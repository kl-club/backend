package kl.club.service.Service;

import kl.club.service.Exception.UsuarioCpfExistenteException;
import kl.club.service.Exception.UsuarioTelefoneExistenteException;
import kl.club.service.Model.DTO.UsuarioRequestDTO;
import kl.club.service.Model.DTO.UsuarioResponseDTO;
import kl.club.service.Model.Entity.Usuario;
import kl.club.service.Model.Mapper.UsuarioMapper;
import kl.club.service.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
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
}
