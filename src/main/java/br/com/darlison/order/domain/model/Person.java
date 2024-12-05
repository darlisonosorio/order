package br.com.darlison.order.domain.model;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class Person extends BaseModel {

    private String name;
    private LocalDate birthDate;
    private String cpf;
    private Boolean worker;
    private List<Project> projects;

    public Person() { }

    public Person(UUID id) {
        super(id);
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(final LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(final String cpf) {
        this.cpf = cpf;
    }

    public Boolean getWorker() {
        return worker;
    }

    public void setWorker(final Boolean worker) {
        this.worker = worker;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(final List<Project> projects) {
        this.projects = projects;
    }

}
