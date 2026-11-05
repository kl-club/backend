package kl.club.service.Model.DTO;

import kl.club.service.Model.enums.NivelFuncionario;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados do funcionário (a senha nunca é devolvida)")
public record FuncionarioResponseDTO(

        @Schema(description = "ID do funcionário", example = "1")
        Integer idFuncionario,
        @Schema(description = "Nome do funcionário", example = "Juliana Ribeiro")
        String nome,
        @Schema(description = "E-mail de acesso", example = "juliana.ribeiro@klclub.com.br")
        String email,
        @Schema(description = "Nível de acesso", example = "ADMINISTRADOR")
        NivelFuncionario nivel,
        @Schema(description = "false quando o funcionário foi desativado (não consegue mais fazer login)", example = "true")
        Boolean ativo
) {
}
