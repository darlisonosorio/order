package br.com.darlison.order.domain.port.repository;

import br.com.darlison.order.domain.model.Page;
import br.com.darlison.order.domain.model.Person;

import java.util.Optional;
import java.util.UUID;

public interface PersonRepository {

    UUID save(Person person);

    Page<Person> findAll(String description, int page, int limit);

    Optional<Person> findById(UUID id);

}
