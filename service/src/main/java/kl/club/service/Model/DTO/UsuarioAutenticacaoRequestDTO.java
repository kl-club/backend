package kl.club.service.Model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioAutenticacaoRequestDTO(

        @NotBlank
        @Size(min = 11, max = 11)
        String cpf
) {
}
