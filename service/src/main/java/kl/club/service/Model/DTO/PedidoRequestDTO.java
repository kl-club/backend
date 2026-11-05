package kl.club.service.Model.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kl.club.service.Model.enums.FormaPagamento;
import kl.club.service.Model.enums.Modalidade;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Corpo do POST /pedidos. Não recebe preço, total, pontos, status nem data:
 * esses valores são sempre calculados pelo back-end.
 */
@Schema(description = "Dados para criar um pedido. Preço, total, pontos, status e data são calculados pela API")
public record PedidoRequestDTO(

        @Schema(description = "ID do cliente (opcional). Sem cliente, o pedido não gera pontos", example = "1")
        Integer idUsuario,

        @Schema(description = "Comer no local ou retirar", example = "LOCAL")
        @NotNull(message = "A modalidade é obrigatória")
        Modalidade modalidade,

        @Schema(description = "Forma de pagamento. DINHEIRO nasce PENDENTE; as demais, PAGO", example = "PIX")
        @NotNull(message = "A forma de pagamento é obrigatória")
        FormaPagamento formaPagamento,

        @Schema(description = "Observações do cliente (opcional), até 500 caracteres", example = "Sem orégano, por favor.")
        @Size(max = 500, message = "As observações devem ter no máximo 500 caracteres")
        String observacoes,

        @Schema(description = "De 1 a 50 itens, cada produto uma única vez")
        @NotEmpty(message = "O pedido deve ter pelo menos 1 item")
        @Size(max = 50, message = "O pedido deve ter no máximo 50 itens")
        List<@NotNull(message = "O item é obrigatório") @Valid ItemPedidoRequestDTO> itens
) {
}
