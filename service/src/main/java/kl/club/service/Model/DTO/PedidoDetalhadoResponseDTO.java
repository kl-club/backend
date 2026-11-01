package kl.club.service.Model.DTO;

import kl.club.service.Model.enums.FormaPagamento;
import kl.club.service.Model.enums.Modalidade;
import kl.club.service.Model.enums.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoDetalhadoResponseDTO(
        Integer idPedido,
        LocalDateTime dataHora,
        Modalidade modalidade,
        StatusPedido statusPedido,
        FormaPagamento formaPagamento,
        BigDecimal valorTotal,
        Integer pontosGerados,
        String observacoes,
        List<ItemPedidoResponseDTO> itens
) {
}