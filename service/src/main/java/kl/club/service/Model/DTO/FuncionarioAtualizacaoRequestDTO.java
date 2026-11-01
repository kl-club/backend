package kl.club.service.Model.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kl.club.service.Model.enums.NivelFuncionario;

public record FuncionarioAtualizacaoRequestDTO(

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,

        @NotBlank(message = "O email é obrigatório")
        @Email(message = "O email é inválido")
        @Size(max = 100, message = "O email deve ter no máximo 100 caracteres")
        String email,

        @NotNull(message = "O nível é obrigatório")
        NivelFuncionario nivel
) {
}
