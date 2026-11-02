package kl.club.service.Model.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para cadastro e atualização de categorias")
public record CategoriaRequestDTO(

        @Schema(
                description = "Nome da categoria",
                example = "Bebidas",
                maxLength = 100
        )
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome

) {}
