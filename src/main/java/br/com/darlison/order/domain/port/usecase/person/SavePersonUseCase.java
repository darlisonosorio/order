package br.com.darlison.order.domain.port.usecase.person;

import br.com.darlison.order.domain.model.Person;

public interface SavePersonUseCase {

    void save(Person person);

}
