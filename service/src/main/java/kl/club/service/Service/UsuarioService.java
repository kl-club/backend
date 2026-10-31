package kl.club.service.Service;

import kl.club.service.Exception.UsuarioCpfExistenteException;
import kl.club.service.Exception.UsuarioTelefoneExistenteException;
import kl.club.service.Model.DTO.UsuarioRequestDTO;
import kl.club.service.Model.DTO.UsuarioResponseDTO;
import kl.club.service.Model.Entity.Usuario;
import kl.club.service.Model.Mapper.UsuarioMapper;
import kl.club.service.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UsuarioService {


    private final UsuarioRepository usuarioRepository;


    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }


    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO requestDTO) {

        if (usuarioRepository.existsByCpf(requestDTO.getCpf())) {
            throw new UsuarioCpfExistenteException(requestDTO.getCpf());
        } else if (usuarioRepository.existsByTelefone(requestDTO.getTelefone())) {
            throw new UsuarioTelefoneExistenteException(requestDTO.getTelefone());
        }

        Usuario usuarioCadastrar = UsuarioMapper.toEntity(requestDTO);
        usuarioCadastrar.setContaCriada(LocalDateTime.now());

        return UsuarioMapper.toResponse(usuarioRepository.save(usuarioCadastrar));
    }
}
