package kl.club.service.Model.DTO;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Cliente do pedido")
public record PedidoUsuarioResponseDTO(
        @Schema(description = "ID do cliente", example = "1")
        Integer idUsuario,
        @Schema(description = "Nome do cliente", example = "Ana Beatriz Souza")
        String nome,
        @Schema(description = "Telefone do cliente", example = "11987654321")
        String telefone
) {
}
