package br.com.darlison.order.domain.exceptions;

public class SaveOrderException extends RuntimeException {

    public SaveOrderException(final String message) {
        super(message);
    }

}
