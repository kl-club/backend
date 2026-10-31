package kl.club.service.Model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public record UsuarioRequestDTO(

        @NotBlank
        @Size(max = 100)
        String nome,

        @NotBlank
        @CPF
        @Size(min = 11, max = 11)
        String cpf,

        @NotBlank
        @Pattern(regexp = "\\d{11}")
        String telefone
) {
}
