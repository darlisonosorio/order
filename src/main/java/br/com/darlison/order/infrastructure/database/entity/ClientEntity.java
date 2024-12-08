package br.com.darlison.order.infrastructure.database.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "tb_client")
@EntityListeners(AuditingEntityListener.class)
public class ClientEntity extends BaseEntity {

    @Column(name = "tx_name", length = 256, nullable = false)
    private String name;

    @Column(name = "tx_email", length = 256, unique = true, nullable = false)
    private String email;

    @JsonIgnore
    @JsonBackReference
    @OneToMany(mappedBy = "clientEntity", cascade = CascadeType.ALL)
    private List<OrderEntity> orderEntities;

    public ClientEntity(UUID id) {
        super(id);
    }

}