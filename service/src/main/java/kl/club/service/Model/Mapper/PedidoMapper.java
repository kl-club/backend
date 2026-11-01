package kl.club.service.Model.Mapper;

import kl.club.service.Model.DTO.ItemPedidoResponseDTO;
import kl.club.service.Model.DTO.PedidoDetalhadoResponseDTO;
import kl.club.service.Model.Entity.Pedido;

import java.util.List;

public class PedidoMapper {

    public static PedidoDetalhadoResponseDTO toResponseDetalhado(Pedido pedido) {

        List<ItemPedidoResponseDTO> itens = pedido.getItens()
                .stream()
                .map(ItemPedidoMapper::toResponse)
                .toList();

        return new PedidoDetalhadoResponseDTO(
                pedido.getIdPedido(),
                pedido.getDataHora(),
                pedido.getModalidade(),
                pedido.getStatusPedido(),
                pedido.getFormaPagamento(),
                pedido.getValorTotal(),
                pedido.getPontosGerados(),
                pedido.getObservacoes(),
                itens
        );
    }
}
