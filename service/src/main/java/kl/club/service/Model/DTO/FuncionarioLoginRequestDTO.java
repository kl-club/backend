package kl.club.service.Model.DTO;

import jakarta.validation.constraints.NotBlank;

public record FuncionarioLoginRequestDTO(

        @NotBlank(message = "O email é obrigatório")
        String email,

        @NotBlank(message = "A senha é obrigatória")
        String senha
) {
}
