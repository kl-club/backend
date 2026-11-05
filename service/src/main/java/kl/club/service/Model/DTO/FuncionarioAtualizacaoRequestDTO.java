package kl.club.service.Model.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kl.club.service.Model.enums.NivelFuncionario;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para atualização de funcionário (não altera a senha nem o status)")
public record FuncionarioAtualizacaoRequestDTO(

        @Schema(description = "Nome do funcionário", example = "Juliana Ribeiro")
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,

        @Schema(description = "E-mail de acesso. Não pode estar em uso por outro funcionário", example = "juliana.ribeiro@klclub.com.br")
        @NotBlank(message = "O email é obrigatório")
        @Email(message = "O email é inválido")
        @Size(max = 100, message = "O email deve ter no máximo 100 caracteres")
        String email,

        @Schema(description = "Nível de acesso", example = "ADMINISTRADOR")
        @NotNull(message = "O nível é obrigatório")
        NivelFuncionario nivel
) {
}
