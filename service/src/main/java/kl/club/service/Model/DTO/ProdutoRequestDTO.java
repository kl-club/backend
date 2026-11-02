package kl.club.service.Model.DTO;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProdutoRequestDTO(

        @NotNull(message = "A categoria é obrigatória")
        Integer idCategoria,

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,

        @Size(max = 21845, message = "A descrição deve ter no máximo 21845 caracteres")
        String descricao,

        @Size(max = 21845, message = "Os ingredientes devem ter no máximo 21845 caracteres")
        String ingredientes,

        @NotNull(message = "O preço é obrigatório")
        @Positive(message = "O preço deve ser maior que zero")
        @Digits(integer = 8, fraction = 2, message = "O preço deve ter no máximo 8 dígitos inteiros e 2 decimais")
        BigDecimal preco,

        @NotNull(message = "A pontuação é obrigatória")
        @PositiveOrZero(message = "A pontuação não pode ser negativa")
        Integer pontuacao,

        @Size(max = 500, message = "A imagem deve ter no máximo 500 caracteres")
        String imagem
) {
}
