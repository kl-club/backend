package kl.club.service.Model.DTO;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para cadastro e atualização de produtos")
public record ProdutoRequestDTO(

        @Schema(description = "ID da categoria do produto", example = "9")
        @NotNull(message = "A categoria é obrigatória")
        Integer idCategoria,

        @Schema(description = "Nome do produto. Único entre os produtos, sem diferenciar maiúsculas e acentos", example = "Bauru")
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,

        @Schema(description = "Descrição do produto (opcional)", example = "Lanche prensado.")
        @Size(max = 21845, message = "A descrição deve ter no máximo 21845 caracteres")
        String descricao,

        @Schema(description = "Ingredientes do produto (opcional)", example = "Pão francês, presunto, queijo mussarela, tomate, orégano, manteiga")
        @Size(max = 21845, message = "Os ingredientes devem ter no máximo 21845 caracteres")
        String ingredientes,

        @Schema(description = "Preço em reais, maior que zero, com até 2 casas decimais", example = "9.50")
        @NotNull(message = "O preço é obrigatório")
        @Positive(message = "O preço deve ser maior que zero")
        @Digits(integer = 8, fraction = 2, message = "O preço deve ter no máximo 8 dígitos inteiros e 2 decimais")
        BigDecimal preco,

        @Schema(description = "Pontos que o cliente ganha por unidade. Informada no cadastro, sem cálculo pelo preço", example = "10")
        @NotNull(message = "A pontuação é obrigatória")
        @PositiveOrZero(message = "A pontuação não pode ser negativa")
        Integer pontuacao,

        @Schema(description = "URL da imagem do produto (opcional)", example = "https://cdn.klclub.com.br/produtos/bauru.jpg")
        @Size(max = 500, message = "A imagem deve ter no máximo 500 caracteres")
        String imagem
) {
}
