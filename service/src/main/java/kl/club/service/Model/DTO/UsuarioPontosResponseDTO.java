package kl.club.service.Model.DTO;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Saldo de pontos do cliente")
public record UsuarioPontosResponseDTO(
        @Schema(description = "ID do cliente", example = "4")
        Integer idUsuario,
        @Schema(description = "Nome do cliente", example = "Lucas Martins")
        String nome,
        @Schema(description = "Soma dos pontos dos pedidos fora de PENDENTE e CANCELADO", example = "60")
        Long pontosAcumulados
) {
}