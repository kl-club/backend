package kl.club.service.Model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para troca de senha do funcionário")
public record FuncionarioSenhaRequestDTO(

        @Schema(description = "Senha atual, para confirmação", example = "Senha@123")
        @NotBlank(message = "A senha atual é obrigatória")
        String senhaAtual,

        @Schema(description = "Nova senha", example = "NovaSenha@456")
        @NotBlank(message = "A nova senha é obrigatória")
        @Size(max = 255, message = "A nova senha deve ter no máximo 255 caracteres")
        String novaSenha
) {
}
