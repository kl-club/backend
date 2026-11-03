package kl.club.service.Model.DTO;

import java.util.List;

/**
 * Envelope de paginação usado pelo histórico e pela tabela do dashboard.
 * {@code pagina} começa em 1.
 */
public record PaginaResponseDTO<T>(
        List<T> conteudo,
        Integer pagina,
        Integer tamanho,
        Long total,
        Integer totalPaginas
) {
}
