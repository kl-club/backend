package kl.club.service.Model.enums;

import java.util.Optional;

public enum StatusPedido {
    PENDENTE,
    PAGO,
    EM_PREPARO,
    PRONTO_PARA_RETIRADA,
    CONCLUIDO,
    CANCELADO;

    /**
     * Próximo status ao avançar o pedido. Vazio quando o pedido não pode avançar
     * (CONCLUIDO ou CANCELADO).
     */
    public Optional<StatusPedido> proximo() {
        return switch (this) {
            case PENDENTE, PAGO -> Optional.of(EM_PREPARO);
            case EM_PREPARO -> Optional.of(PRONTO_PARA_RETIRADA);
            case PRONTO_PARA_RETIRADA -> Optional.of(CONCLUIDO);
            case CONCLUIDO, CANCELADO -> Optional.empty();
        };
    }

    /** O cancelamento só parte dos novos pedidos. */
    public boolean podeSerCancelado() {
        return this == PENDENTE || this == PAGO;
    }
}
