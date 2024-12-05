package br.com.darlison.order.domain.port.repository;

import br.com.darlison.order.domain.model.Page;
import br.com.darlison.order.domain.model.Project;

import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository {

    UUID save(Project project);

    Page<Project> findAll(String description, int page, int limit);

    Optional<Project> findById(UUID id);

}
