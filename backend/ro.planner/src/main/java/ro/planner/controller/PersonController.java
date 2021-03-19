package ro.planner.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.planner.DTO.PersonDTO;
import ro.planner.model.Person;
import ro.planner.service.PersonService;
import ro.planner.service.UserService;

import javax.validation.Valid;

/**
 * This class gets the data from the Person class from model package and implements operations that will be used
 * later in front-end.
 */
@RestController
@RequestMapping("/person")
public class PersonController {
    private PersonService personService;
    private UserService userService;

    @Autowired
    public PersonController(final PersonService personService, final UserService userService) {
        this.personService = personService;
        this.userService = userService;
    }

    /**
     * This method creates/updates a person
     * @param personDTO
     */
    @PostMapping
    @ResponseBody
    void saveOrUpdate(@RequestBody @Valid final PersonDTO personDTO) {
        final ModelMapper modelMapper = new ModelMapper();
        final Person person = modelMapper.map(personDTO, Person.class);
        getPersonService().saveOrUpdate(person);
    }

    public PersonService getPersonService() {
        return personService;
    }

    public UserService getUserService() {
        return userService;
    }
}
