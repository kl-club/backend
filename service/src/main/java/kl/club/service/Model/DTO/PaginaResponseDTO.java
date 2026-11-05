package kl.club.service.Model.DTO;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Envelope de paginação usado pelo histórico e pela tabela do dashboard.
 * {@code pagina} começa em 1.
 */
@Schema(description = "Página de resultados (histórico e tabela do dashboard)")
public record PaginaResponseDTO<T>(
        @Schema(description = "Itens da página")
        List<T> conteudo,
        @Schema(description = "Número da página, a partir de 1", example = "1")
        Integer pagina,
        @Schema(description = "Itens por página (1 a 50)", example = "10")
        Integer tamanho,
        @Schema(description = "Total de itens em todas as páginas", example = "18")
        Long total,
        @Schema(description = "Total de páginas", example = "2")
        Integer totalPaginas
) {
}
