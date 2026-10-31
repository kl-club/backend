package kl.club.service.model.mapper;

import kl.club.service.model.dto.UsuarioRequestDTO;
import kl.club.service.model.dto.UsuarioResponseDTO;
import kl.club.service.model.entity.Usuario;

public final class UsuarioMapper {

    private UsuarioMapper() {
    }

    public static UsuarioResponseDTO toResponse(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getIdUsuario(),
                usuario.getNome(),
                usuario.getCpf(),
                usuario.getTelefone());
    }

    public static Usuario toEntity(UsuarioRequestDTO dto) {

        Usuario usuario = new Usuario();

        usuario.setNome(dto.nome());
        usuario.setCpf(dto.cpf());
        usuario.setTelefone(dto.telefone());

        return usuario;
    }
}
