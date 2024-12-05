package br.com.darlison.order.infrastructure.database.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Getter
@Setter
@NoArgsConstructor
@Entity(name = "tb_person_project")
@EntityListeners(AuditingEntityListener.class)
public class PersonProjectEntity extends BaseEntity {

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_person", referencedColumnName = "id", nullable = false)
    private PersonEntity personEntity;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_project", referencedColumnName = "id", nullable = false)
    private ProjectEntity projectEntity;

    public PersonProjectEntity(PersonEntity person, ProjectEntity project) {
        this.personEntity = person;
        this.projectEntity = project;
    }

}
