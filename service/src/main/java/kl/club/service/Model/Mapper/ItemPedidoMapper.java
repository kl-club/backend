package kl.club.service.Model.Mapper;

import kl.club.service.Model.DTO.ItemPedidoResponseDTO;
import kl.club.service.Model.Entity.ItemPedido;

public final class ItemPedidoMapper {

    private ItemPedidoMapper() {
    }

    public static ItemPedidoResponseDTO toResponse(ItemPedido item) {

        return new ItemPedidoResponseDTO(
                item.getIdItemPedido(),
                item.getQuantidade(),
                item.getPrecoUnitario(),
                item.getPontosUnitario(),
                ProdutoMapper.toResumoResponse(item.getProduto())
        );
    }
}