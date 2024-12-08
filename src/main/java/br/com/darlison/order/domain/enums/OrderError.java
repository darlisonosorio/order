package br.com.darlison.order.domain.enums;

public enum OrderError {
    ORDER_ALREADY_EXISTS("Pedido já cadastrado no sistema."),
    NO_PRODUCTS("Nenhum produto encontrado no pedido."),
    CLIENT_SAVE_ERROR("Erro ao salvar o usuário"),
    ORDER_SAVE_ERROR("Erro ao salvar o pedido");

    private final String message;

    OrderError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}