package kl.club.service.Model.DTO;

import java.math.BigDecimal;

public record UsuarioResumoResponseDTO(
        Integer idUsuario,
        String nome,
        Long quantidadePedidos,
        BigDecimal totalGasto,
        Long pontosAcumulados,
        Integer ultimoPedidoId
) {
}