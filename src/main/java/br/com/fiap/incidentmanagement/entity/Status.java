package br.com.fiap.incidentmanagement.entity;

public enum Status {
    AGUARDANDO("Aguardando atendimento"),
    EM_ATENDIMENTO("Em atendimento"),
    RESOLVIDA("Resolvida"),
    ENCERRADA_SEM_SOLUCAO("Encerrada sem solução");

    private final String description;

    Status(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
