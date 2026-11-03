package kl.club.service.Model.Mapper;

import kl.club.service.Model.DTO.ItemPedidoResponseDTO;
import kl.club.service.Model.DTO.PedidoItemResponseDTO;
import kl.club.service.Model.DTO.PedidoItemResumoResponseDTO;
import kl.club.service.Model.Entity.ItemPedido;
import kl.club.service.Model.Entity.Produto;

public final class ItemPedidoMapper {

    private ItemPedidoMapper() {
    }

    /** Formato usado pelas rotas de /usuarios. */
    public static ItemPedidoResponseDTO toResponse(ItemPedido item) {

        return new ItemPedidoResponseDTO(
                item.getIdItemPedido(),
                item.getQuantidade(),
                item.getPrecoUnitario(),
                item.getPontosUnitario(),
                ProdutoMapper.toResumoResponse(item.getProduto())
        );
    }

    /** Formato usado pelas rotas de /pedidos. */
    public static PedidoItemResponseDTO toPedidoItemResponse(ItemPedido item) {

        return new PedidoItemResponseDTO(
                item.getProduto().getIdProduto(),
                item.getProduto().getNome(),
                item.getQuantidade(),
                item.getPrecoUnitario(),
                item.calcularValorTotal()
        );
    }

    /** Formato da tabela do dashboard e do histórico. */
    public static PedidoItemResumoResponseDTO toPedidoItemResumoResponse(ItemPedido item) {

        return new PedidoItemResumoResponseDTO(
                item.getProduto().getNome(),
                item.getQuantidade()
        );
    }

    /**
     * Cria o item copiando o preço e a pontuação atuais do produto. Assim, o pedido
     * não muda se o produto mudar de preço ou pontuação depois.
     */
    public static ItemPedido toEntity(Produto produto, Integer quantidade) {

        ItemPedido item = new ItemPedido();

        item.setProduto(produto);
        item.setQuantidade(quantidade);
        item.setPrecoUnitario(produto.getPreco());
        item.setPontosUnitario(produto.getPontuacao());

        return item;
    }
}
