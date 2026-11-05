package kl.club.service.Model.DTO;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Cliente com todos os seus pedidos")
public record UsuarioPedidosResponseDTO(
        @Schema(description = "ID do cliente", example = "1")
        Integer idUsuario,
        @Schema(description = "Nome do cliente", example = "Ana Beatriz Souza")
        String nome,
        @Schema(description = "CPF do cliente", example = "12345678909")
        String cpf,
        @Schema(description = "Telefone do cliente", example = "11987654321")
        String telefone,
        @Schema(description = "Pedidos do cliente, de todos os status")
        List<PedidoDetalhadoResponseDTO> pedidos
) {
}
