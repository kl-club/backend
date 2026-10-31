package kl.club.service.Model.Mapper;

import kl.club.service.Model.DTO.UsuarioRequestDTO;
import kl.club.service.Model.DTO.UsuarioResponseDTO;
import kl.club.service.Model.Entity.Usuario;

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
