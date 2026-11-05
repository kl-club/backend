package kl.club.service.Model.DTO;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resumo dos pedidos do cliente")
public record UsuarioResumoResponseDTO(
        @Schema(description = "ID do cliente", example = "3")
        Integer idUsuario,
        @Schema(description = "Nome do cliente", example = "Fernanda Oliveira")
        String nome,
        @Schema(description = "Quantidade de pedidos de todos os status, inclusive cancelados", example = "3")
        Long quantidadePedidos,
        @Schema(description = "Soma do valor dos pedidos CONCLUIDO, em reais", example = "58.0")
        BigDecimal totalGasto,
        @Schema(description = "Saldo de pontos (mesma regra de GET /usuarios/{id}/pontos)", example = "58")
        Long pontosAcumulados,
        @Schema(description = "ID do pedido mais recente; null se o cliente não tem pedidos", example = "17")
        Integer ultimoPedidoId
) {
}