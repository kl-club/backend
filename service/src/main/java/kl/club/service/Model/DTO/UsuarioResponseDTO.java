package kl.club.service.Model.DTO;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados do cliente")
public record UsuarioResponseDTO(

        @Schema(description = "ID do cliente", example = "1")
        Integer idUsuario,
        @Schema(description = "Nome do cliente", example = "Ana Beatriz Souza")
        String nome,
        @Schema(description = "CPF do cliente", example = "12345678909")
        String cpf,
        @Schema(description = "Telefone do cliente", example = "11987654321")
        String telefone
) {
}
