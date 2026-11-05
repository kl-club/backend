package kl.club.service.Model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Login do cliente, só pelo CPF")
public record UsuarioAutenticacaoRequestDTO(

        @Schema(description = "CPF do cliente, só os 11 dígitos", example = "12345678909")
        @NotBlank
        @Size(min = 11, max = 11)
        String cpf
) {
}
