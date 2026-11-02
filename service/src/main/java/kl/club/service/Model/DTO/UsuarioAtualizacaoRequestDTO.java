package kl.club.service.Model.DTO;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsuarioAtualizacaoRequestDTO(

        @Size(max = 100)
        @Pattern(regexp = ".*\\S.*")
        String nome,

        @Pattern(regexp = "\\d{11}")
        String telefone
) {
}