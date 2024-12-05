package br.com.darlison.order.domain.port.usecase.person;

import br.com.darlison.order.domain.model.Page;
import br.com.darlison.order.domain.model.Person;

import java.util.UUID;

public interface GetPersonUseCase {

    Person findById(UUID id);

    Page<Person> findAll(String description, int page, int limit);

}
