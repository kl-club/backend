package kl.club.service.Model.enums;

public enum FormaPagamento {
    PIX,
    CREDITO,
    DEBITO,
    VOUCHER,
    DINHEIRO;

    /**
     * Status com que o pedido nasce. Regra provisória, enquanto não há integração
     * de pagamentos: o totem só cria o pedido depois da aprovação, então só
     * DINHEIRO (pago no balcão) nasce PENDENTE.
     */
    public StatusPedido statusInicial() {
        return this == DINHEIRO ? StatusPedido.PENDENTE : StatusPedido.PAGO;
    }
}
