package ro.planner.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.planner.model.Person;
import ro.planner.repository.PersonRepository;
import ro.planner.service.PersonService;

/**
 * This class creates the logic of the application for the Person model by using the methods from PersonRepository
 */
@Service("personService")
public class PersonServiceImpl implements PersonService {

    private PersonRepository personRepository;

    @Autowired
    public PersonServiceImpl(final PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /**
     * This method creates/updates a person
     * @param person
     */
    @Override
    @Transactional
    public void saveOrUpdate(final Person person) {
        getPersonRepository().save(person);
    }

    /**
     * This method gets the person that has the id = id
     * @param id
     * @return the person
     */
    @Override
    @Transactional
    public Person getById(final Long id) {
        return getPersonRepository().getById(id);
    }

    public PersonRepository getPersonRepository() {
        return personRepository;
    }
}
