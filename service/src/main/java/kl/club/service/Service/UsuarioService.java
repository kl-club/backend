package kl.club.service.service;

import kl.club.service.exception.UsuarioCpfExistenteException;
import kl.club.service.exception.UsuarioTelefoneExistenteException;
import kl.club.service.model.dto.UsuarioRequestDTO;
import kl.club.service.model.dto.UsuarioResponseDTO;
import kl.club.service.model.entity.Usuario;
import kl.club.service.model.mapper.UsuarioMapper;
import kl.club.service.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO requestDTO) {

        if (usuarioRepository.existsByCpf(requestDTO.cpf())) {
            throw new UsuarioCpfExistenteException(requestDTO.cpf());
        }

        if (usuarioRepository.existsByTelefone(requestDTO.telefone())) {
            throw new UsuarioTelefoneExistenteException(requestDTO.telefone());
        }

        Usuario usuarioCadastrar = UsuarioMapper.toEntity(requestDTO);

        return UsuarioMapper.toResponse(usuarioRepository.save(usuarioCadastrar));
    }
}
