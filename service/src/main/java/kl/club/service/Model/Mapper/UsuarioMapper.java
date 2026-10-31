package kl.club.service.Model.Mapper;

import kl.club.service.Model.DTO.UsuarioRequestDTO;
import kl.club.service.Model.DTO.UsuarioResponseDTO;
import kl.club.service.Model.Entity.Usuario;

public class UsuarioMapper {

    public static UsuarioResponseDTO toResponse(Usuario usuario){

        UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO();

        usuarioResponseDTO.setIdUsuario(usuario.getIdUsuario());
        usuarioResponseDTO.setCpf(usuario.getCpf());
        usuarioResponseDTO.setNome(usuario.getNome());
        usuarioResponseDTO.setTelefone(usuario.getTelefone());

        return usuarioResponseDTO;
    }

    public static Usuario toEntity(UsuarioRequestDTO dto){

        Usuario usuario = new Usuario();

        usuario.setNome(dto.getNome());
        usuario.setCpf(dto.getCpf());
        usuario.setTelefone(dto.getTelefone());

        return usuario;
    }

}
