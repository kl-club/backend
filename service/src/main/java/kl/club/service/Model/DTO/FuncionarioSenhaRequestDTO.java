package kl.club.service.Model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FuncionarioSenhaRequestDTO(

        @NotBlank(message = "A senha atual é obrigatória")
        String senhaAtual,

        @NotBlank(message = "A nova senha é obrigatória")
        @Size(max = 255, message = "A nova senha deve ter no máximo 255 caracteres")
        String novaSenha
) {
}
