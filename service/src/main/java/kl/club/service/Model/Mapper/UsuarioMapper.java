package kl.club.service.Model.Mapper;

import kl.club.service.Model.DTO.PedidoDetalhadoResponseDTO;
import kl.club.service.Model.DTO.UsuarioPedidosResponseDTO;
import kl.club.service.Model.DTO.UsuarioRequestDTO;
import kl.club.service.Model.DTO.UsuarioResponseDTO;
import kl.club.service.Model.Entity.Usuario;

import java.util.List;

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

    public static UsuarioPedidosResponseDTO toResponseComPedidos(Usuario usuario) {

        List<PedidoDetalhadoResponseDTO> pedidos = usuario.getPedidos()
                .stream()
                .map(PedidoMapper::toResponseDetalhado)
                .toList();

        return new UsuarioPedidosResponseDTO(
                usuario.getIdUsuario(),
                usuario.getNome(),
                usuario.getCpf(),
                usuario.getTelefone(),
                pedidos
        );
    }
}
