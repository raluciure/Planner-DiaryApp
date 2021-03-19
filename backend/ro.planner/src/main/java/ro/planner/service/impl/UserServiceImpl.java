package ro.planner.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.planner.model.User;
import ro.planner.repository.PersonRepository;
import ro.planner.repository.UserRepository;
import ro.planner.service.UserService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * This class creates the logic of the application for the User model by using the methods from UserRepository
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PersonRepository personRepository;

    @Autowired
    public UserServiceImpl(final UserRepository userRepository,
                           final PersonRepository personRepository) {
        this.userRepository = userRepository;
        this.personRepository = personRepository;
    }

    /**
     * This method creates/updates a user
     * @param user
     */
    @Override
    @Transactional
    public void saveOrUpdate(final User user) {
        if (user.getPerson().getId() == null) {
            getPersonRepository().save(user.getPerson());
        }
        if(user.getPassword().isEmpty()) {
            user.setPassword(getUserRepository().getById(user.getId()).getPassword());
        }
        else {
            user.setPassword(getPasswordEncoder().encode(user.getPassword()));
        }
        getUserRepository().save(user);
    }

    /**
     * This method gets the user that has the id = id
     * @param id
     * @return the user
     */
    @Override
    @Transactional
    public User getById(final Long id) {
        return getUserRepository().getById(id);
    }

    /**
     * This method gets the user that has the username = username
     * @param username
     * @return the user
     * @throws UsernameNotFoundException
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return getUserRepository().getByUsername(username);
    }

    /**
     * This method implements the login operation for the loginUser
     * @param loginUser
     * @return the user
     */
    @Override
    public User login(User loginUser) {
        final User userModel = getUserRepository().getByUsername(loginUser.getUsername());
        if (userModel == null) {
            throw new EntityNotFoundException("INVALID_CREDENTIALS");
        }

        if (!getPasswordEncoder().matches(loginUser.getPassword(), userModel.getPassword())) {
            throw new EntityNotFoundException("INVALID_CREDENTIALS");
        }

        loginUser.setId(userModel.getId());
        loginUser.setRole(userModel.getRole());

        return loginUser;
    }

    /**
     * This method gets all the users from the data base
     * @return the list of all users
     */
    @Override
    public List<User> getAll() {
        return getUserRepository().findAll();
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public PersonRepository getPersonRepository() {
        return personRepository;
    }

    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
