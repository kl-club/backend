package kl.club.service.Model.DTO;

import jakarta.validation.constraints.NotBlank;

public record FuncionarioLoginRequestDTO(

        @NotBlank
        String email,

        @NotBlank
        String senha
) {
}
