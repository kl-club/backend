package kl.club.service.Model.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kl.club.service.Model.enums.NivelFuncionario;

public record FuncionarioRequestDTO(

        @NotBlank
        @Size(max = 100)
        String nome,

        @NotBlank
        @Email
        @Size(max = 100)
        String email,

        @NotBlank
        @Size(max = 255)
        String senha,

        @NotNull
        NivelFuncionario nivel
) {
}
