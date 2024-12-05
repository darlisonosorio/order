package br.com.darlison.order.domain.exceptions;

import br.com.darlison.order.common.exception.BadRequestException;

public class PersonNotFoundException extends BadRequestException {

    public PersonNotFoundException() {
        super("EXC-PESS-NF", "Pessoa não encontrada.");
    }

}
