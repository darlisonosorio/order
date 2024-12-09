package br.com.darlison.order.infrastructure.database.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tb_product")
@EntityListeners(AuditingEntityListener.class)
public class ProductEntity extends BaseEntity {

    @Column(name = "tx_name", length = 256, nullable = false)
    private String name;

    @Column(name = "num_unit_price", nullable = false)
    private BigDecimal price;

    @Column(name = "num_quantity", nullable = false)
    private int quantity;

    @Column(name = "num_total_price")
    private BigDecimal totalPrice;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_order", referencedColumnName = "id", nullable = false)
    private OrderEntity orderEntity;

    public ProductEntity(UUID id) {
        super(id);
    }

}