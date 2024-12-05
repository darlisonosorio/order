package br.com.darlison.order.domain.port.usecase.project;

import br.com.darlison.order.domain.model.Page;
import br.com.darlison.order.domain.model.Project;

import java.util.UUID;

public interface GetProjectUseCase {

    Project findById(UUID id);

    Page<Project> findAll(String description, int page, int limit);

}
