package br.com.darlison.order.infrastructure.database.entity;

import br.com.darlison.order.domain.enums.ProjectStatusEnum;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "tb_project")
@EntityListeners(AuditingEntityListener.class)
public class ProjectEntity extends BaseEntity {

    @Column(name="tx_name", nullable = false)
    private String name;
    @Column(name="tx_description", nullable = false)
    private String description;
    @Column(name="dt_begin_date", nullable = false)
    private LocalDate beginDate;
    @Column(name="dt_expected_end_date", nullable = false)
    private LocalDate expectedEndDate;
    @Column(name="dt_end_date")
    private LocalDate endDate;
    @Column(name="tx_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProjectStatusEnum status;
    @Column(name="nb_budget", nullable = false)
    private BigDecimal budget;
    @Column(name="tx_risk", nullable = false)
    private String risk;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_manager", referencedColumnName = "id", nullable = false)
    private PersonEntity managerEntity;

    @JsonManagedReference
    @OneToMany(mappedBy = "projectEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PersonProjectEntity> memberList;

    public ProjectEntity(UUID id) {
        super(id);
    }

}
