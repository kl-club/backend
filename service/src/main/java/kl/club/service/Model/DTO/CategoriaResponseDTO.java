package kl.club.service.Model.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados completos da categoria")
public record CategoriaResponseDTO(

        @Schema(description = "ID da categoria", example = "1")
        Integer idCategoria,

        @Schema(description = "Nome da categoria", example = "Bebidas")
        String nome,

        @Schema(description = "Status da categoria", example = "true")
        Boolean ativo

) {}
