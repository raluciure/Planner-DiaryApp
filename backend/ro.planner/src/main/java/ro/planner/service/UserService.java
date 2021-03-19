package ro.planner.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ro.planner.model.User;

import java.util.List;

/**
 * This interface contains the methods declared in UserRepository
 */
public interface UserService extends UserDetailsService {
    void saveOrUpdate(final User user);
    User getById (final Long id);
    User login (final User user);
    List<User> getAll();
}
