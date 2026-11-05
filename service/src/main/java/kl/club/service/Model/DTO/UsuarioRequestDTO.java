package kl.club.service.Model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para cadastro de cliente")
public record UsuarioRequestDTO(

        @Schema(description = "Nome do cliente, até 100 caracteres", example = "Ana Beatriz Souza")
        @NotBlank
        @Size(max = 100)
        String nome,

        @Schema(description = "CPF válido, só os 11 dígitos. Único entre os clientes", example = "12345678909")
        @NotBlank
        @CPF
        @Size(min = 11, max = 11)
        String cpf,

        @Schema(description = "Telefone com DDD, só os 11 dígitos. Único entre os clientes", example = "11987654321")
        @NotBlank
        @Pattern(regexp = "\\d{11}")
        String telefone
) {
}
