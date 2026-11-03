package kl.club.service.Model.DTO;

import kl.club.service.Model.enums.Modalidade;

import java.time.LocalDate;

/**
 * Filtros do GET /pedidos/historico, já convertidos pelo controller. Todos são opcionais,
 * exceto pagina e tamanho, que têm valor padrão. A validação das regras fica no service.
 */
public record HistoricoFiltroDTO(
        Integer idPedido,
        String nomeUsuario,
        LocalDate dataInicio,
        LocalDate dataFim,
        Modalidade modalidade,
        String ordenarPor,
        String direcao,
        Integer pagina,
        Integer tamanho
) {
}
