package kl.club.service.Model.DTO;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resultado da verificação de CPF e telefone")
public record UsuarioVerificacaoResponseDTO(
        @Schema(description = "true se o CPF já está cadastrado", example = "true")
        Boolean cpfEmUso,
        @Schema(description = "true se o telefone já está cadastrado", example = "false")
        Boolean telefoneEmUso
) {
}