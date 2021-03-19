package ro.planner.service;

import ro.planner.model.Person;

/**
 * This interface contains the methods declared in PersonRepository
 */
public interface PersonService {
    void saveOrUpdate(final Person person);
    Person getById(final Long id);
}
