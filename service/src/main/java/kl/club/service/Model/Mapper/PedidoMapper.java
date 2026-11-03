package kl.club.service.Model.Mapper;

import kl.club.service.Model.DTO.ItemPedidoResponseDTO;
import kl.club.service.Model.DTO.PedidoDashboardResponseDTO;
import kl.club.service.Model.DTO.PedidoDetalhadoResponseDTO;
import kl.club.service.Model.DTO.PedidoHistoricoResponseDTO;
import kl.club.service.Model.DTO.PedidoItemResponseDTO;
import kl.club.service.Model.DTO.PedidoItemResumoResponseDTO;
import kl.club.service.Model.DTO.PedidoRequestDTO;
import kl.club.service.Model.DTO.PedidoResponseDTO;
import kl.club.service.Model.DTO.PedidoUsuarioResponseDTO;
import kl.club.service.Model.Entity.Pedido;
import kl.club.service.Model.Entity.Usuario;

import java.util.List;

public final class PedidoMapper {

    private PedidoMapper() {
    }

    /** Formato usado pelas rotas de /usuarios. */
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

    /** Formato usado pelas rotas de /pedidos. */
    public static PedidoResponseDTO toResponse(Pedido pedido) {

        List<PedidoItemResponseDTO> itens = pedido.getItens()
                .stream()
                .map(ItemPedidoMapper::toPedidoItemResponse)
                .toList();

        return new PedidoResponseDTO(
                pedido.getIdPedido(),
                pedido.getDataHora(),
                pedido.getModalidade(),
                pedido.getStatusPedido(),
                pedido.getFormaPagamento(),
                pedido.getValorTotal(),
                pedido.getPontosGerados(),
                pedido.getObservacoes(),
                toUsuarioResponse(pedido.getUsuario()),
                itens
        );
    }

    /** Formato do histórico: itens só com nome e quantidade. */
    public static PedidoHistoricoResponseDTO toHistoricoResponse(Pedido pedido) {

        List<PedidoItemResumoResponseDTO> itens = pedido.getItens()
                .stream()
                .map(ItemPedidoMapper::toPedidoItemResumoResponse)
                .toList();

        return new PedidoHistoricoResponseDTO(
                pedido.getIdPedido(),
                pedido.getStatusPedido(),
                pedido.getModalidade(),
                pedido.getDataHora(),
                toUsuarioResponse(pedido.getUsuario()),
                itens,
                pedido.getFormaPagamento(),
                pedido.getValorTotal(),
                pedido.getPontosGerados()
        );
    }

    /** Formato da tabela do dashboard: só o nome do cliente e itens com nome e quantidade. */
    public static PedidoDashboardResponseDTO toDashboardResponse(Pedido pedido) {

        List<PedidoItemResumoResponseDTO> itens = pedido.getItens()
                .stream()
                .map(ItemPedidoMapper::toPedidoItemResumoResponse)
                .toList();

        return new PedidoDashboardResponseDTO(
                pedido.getIdPedido(),
                pedido.getStatusPedido(),
                pedido.getUsuario() == null ? null : pedido.getUsuario().getNome(),
                itens,
                pedido.getFormaPagamento(),
                pedido.getValorTotal(),
                pedido.getPontosGerados()
        );
    }

    /**
     * Copia só os dados informados pelo cliente. Status, data, valor total, pontos,
     * usuário e itens são definidos pelo service.
     */
    public static Pedido toEntity(PedidoRequestDTO dto) {

        Pedido pedido = new Pedido();

        pedido.setModalidade(dto.modalidade());
        pedido.setFormaPagamento(dto.formaPagamento());
        pedido.setObservacoes(dto.observacoes() == null || dto.observacoes().isBlank() ? null : dto.observacoes());

        return pedido;
    }

    private static PedidoUsuarioResponseDTO toUsuarioResponse(Usuario usuario) {

        if (usuario == null) {
            return null;
        }

        return new PedidoUsuarioResponseDTO(
                usuario.getIdUsuario(),
                usuario.getNome(),
                usuario.getTelefone()
        );
    }
}
