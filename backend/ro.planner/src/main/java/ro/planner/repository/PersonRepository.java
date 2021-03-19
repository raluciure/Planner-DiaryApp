package ro.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.planner.model.Person;

/**
 * This interface contains all the operations needed to manipulate data from the database for the Person object
 */
public interface PersonRepository extends JpaRepository<Person, Long> {
    Person getById(final Long id);
}
