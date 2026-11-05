package kl.club.service.Model.DTO;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para atualização do cliente. Informe nome e/ou telefone; corpo sem nenhum dos dois retorna 400")
public record UsuarioAtualizacaoRequestDTO(

        @Schema(description = "Novo nome (opcional), até 100 caracteres e não pode ficar em branco", example = "Ana Beatriz Souza Lima")
        @Size(max = 100)
        @Pattern(regexp = ".*\\S.*")
        String nome,

        @Schema(description = "Novo telefone (opcional), só os 11 dígitos. Não pode estar em uso por outro cliente", example = "11912345678")
        @Pattern(regexp = "\\d{11}")
        String telefone
) {
}