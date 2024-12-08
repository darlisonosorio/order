package br.com.darlison.order.infrastructure.database.entity;


import br.com.darlison.order.domain.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "tb_order")
@EntityListeners(AuditingEntityListener.class)
public class OrderEntity extends BaseEntity {

    @Column(name = "tx_order_id", unique = true, nullable = false)
    private String orderId;

    @Column(name = "num_total_price")
    private BigDecimal totalPrice;

    @Enumerated(value=EnumType.STRING)
    @Column(name = "tx_status")
    private OrderStatus status;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_client", referencedColumnName = "id", nullable = false)
    private ClientEntity clientEntity;

    @JsonIgnore
    @JsonBackReference
    @OneToMany(mappedBy = "orderEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductEntity> productEntities;

    public OrderEntity(UUID id) {
        super(id);
    }

}