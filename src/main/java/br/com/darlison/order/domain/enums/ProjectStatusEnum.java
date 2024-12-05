package br.com.darlison.order.domain.enums;

public enum ProjectStatusEnum {

    EM_ANALISE("Em Análise"),
    ANALISE_REALIZADA("Análise Realizada"),
    ANALISE_APROVADA("Análise Aprovada"),
    INICIANDO("Iniciado"),
    PLANEJANDO("Planejado"),
    EM_ANDAMENTO("Em Andamento"),
    ENCERRADO("Encerrado"),
    CANCELADO("Cancelado");

    private final String description;

    ProjectStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

}
