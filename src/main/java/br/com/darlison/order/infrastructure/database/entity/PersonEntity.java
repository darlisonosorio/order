package br.com.darlison.order.infrastructure.database.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "tb_person")
@EntityListeners(AuditingEntityListener.class)
public class PersonEntity extends BaseEntity {

    @Column(name = "tx_name", nullable = false)
    private String name;
    @Column(name = "dt_birth_date", nullable = false)
    private LocalDate birthDate;
    @Column(name = "tx_cpf", nullable = false)
    private String cpf;
    @Column(name = "is_worker")
    private Boolean worker;

    @JsonIgnore
    @JsonBackReference
    @OneToMany(mappedBy = "managerEntity", cascade = CascadeType.ALL)
    private List<ProjectEntity> managedProjectList;
    @JsonIgnore
    @JsonBackReference
    @OneToMany(mappedBy = "personEntity", cascade = CascadeType.ALL)
    private List<PersonProjectEntity> projectList;

    public PersonEntity(UUID id) {
        super(id);
    }
}
