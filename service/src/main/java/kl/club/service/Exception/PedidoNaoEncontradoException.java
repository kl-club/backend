package kl.club.service.Exception;

public class PedidoNaoEncontradoException extends NegocioException {
    public PedidoNaoEncontradoException() {
        super("PEDIDO_NAO_ENCONTRADO", "Pedido não encontrado");
    }

    public PedidoNaoEncontradoException(String message) {
        super("PEDIDO_NAO_ENCONTRADO", message);
    }
}
