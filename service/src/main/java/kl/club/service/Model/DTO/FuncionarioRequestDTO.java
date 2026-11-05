package kl.club.service.Model.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kl.club.service.Model.enums.NivelFuncionario;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para cadastro de funcionário")
public record FuncionarioRequestDTO(

        @Schema(description = "Nome do funcionário", example = "Juliana Ribeiro")
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,

        @Schema(description = "E-mail de acesso ao dashboard. Único entre os funcionários", example = "juliana.ribeiro@klclub.com.br")
        @NotBlank(message = "O email é obrigatório")
        @Email(message = "O email é inválido")
        @Size(max = 100, message = "O email deve ter no máximo 100 caracteres")
        String email,

        @Schema(description = "Senha de acesso (gravada em texto puro, por decisão do projeto)", example = "Senha@123")
        @NotBlank(message = "A senha é obrigatória")
        @Size(max = 255, message = "A senha deve ter no máximo 255 caracteres")
        String senha,

        @Schema(description = "Nível de acesso. Qualquer valor é aceito, sem controle de permissão", example = "OPERACIONAL")
        @NotNull(message = "O nível é obrigatório")
        NivelFuncionario nivel
) {
}
