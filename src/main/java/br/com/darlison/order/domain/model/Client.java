package br.com.darlison.order.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Client extends BaseModel {

    private String name;

    private String email;

    public Client(UUID id) {
        super(id);
    }

    public Client() {
    }

    public Client(UUID id, String name, String email, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(id, createdAt, updatedAt);
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}