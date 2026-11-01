package kl.club.service.Model.Mapper;

import kl.club.service.Model.DTO.ItemPedidoResponseDTO;
import kl.club.service.Model.DTO.PedidoDetalhadoResponseDTO;
import kl.club.service.Model.DTO.ProdutoResumoResponseDTO;
import kl.club.service.Model.Entity.ItemPedido;
import kl.club.service.Model.Entity.Pedido;
import kl.club.service.Model.Entity.Produto;

import java.util.List;

public class PedidoMapper {

    public static PedidoDetalhadoResponseDTO toResponseDetalhado(Pedido pedido) {

        List<ItemPedidoResponseDTO> itens = pedido.getItens()
                .stream()
                .map(PedidoMapper::toItemResponse)
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

    public static ItemPedidoResponseDTO toItemResponse(ItemPedido item) {

        Produto produto = item.getProduto();

        ProdutoResumoResponseDTO produtoDTO =
                new ProdutoResumoResponseDTO(
                        produto.getIdProduto(),
                        produto.getNome()
                );

        return new ItemPedidoResponseDTO(
                item.getIdItemPedido(),
                item.getQuantidade(),
                item.getPrecoUnitario(),
                item.getPontosUnitario(),
                produtoDTO
        );
    }
}
