package br.com.darlison.order.domain.exceptions;

import br.com.darlison.order.common.exception.BadRequestException;

public class ProjectNotFoundException extends BadRequestException {

    public ProjectNotFoundException() {
        super("EXC-PROJ-NF", "Projeto não encontrado.");
    }

}
