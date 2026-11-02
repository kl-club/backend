package kl.club.service.Model.DTO;

import java.util.List;

public record UsuarioPedidosResponseDTO(
        Integer idUsuario,
        String nome,
        String cpf,
        String telefone,
        List<PedidoDetalhadoResponseDTO> pedidos
) {
}
