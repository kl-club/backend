package kl.club.service.Model.DTO;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Credenciais de login do funcionário")
public record FuncionarioLoginRequestDTO(

        @Schema(description = "E-mail de acesso", example = "juliana.ribeiro@klclub.com.br")
        @NotBlank(message = "O email é obrigatório")
        String email,

        @Schema(description = "Senha de acesso", example = "Senha@123")
        @NotBlank(message = "A senha é obrigatória")
        String senha
) {
}
