package kl.club.service.Model.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kl.club.service.Model.enums.FormaPagamento;
import kl.club.service.Model.enums.Modalidade;

import java.util.List;

/**
 * Corpo do POST /pedidos. Não recebe preço, total, pontos, status nem data:
 * esses valores são sempre calculados pelo back-end.
 */
public record PedidoRequestDTO(

        Integer idUsuario,

        @NotNull(message = "A modalidade é obrigatória")
        Modalidade modalidade,

        @NotNull(message = "A forma de pagamento é obrigatória")
        FormaPagamento formaPagamento,

        @Size(max = 500, message = "As observações devem ter no máximo 500 caracteres")
        String observacoes,

        @NotEmpty(message = "O pedido deve ter pelo menos 1 item")
        @Size(max = 50, message = "O pedido deve ter no máximo 50 itens")
        List<@NotNull(message = "O item é obrigatório") @Valid ItemPedidoRequestDTO> itens
) {
}
