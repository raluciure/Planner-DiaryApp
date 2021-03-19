package ro.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.planner.model.User;

/**
 * This interface contains all the operations needed to manipulate data from the database for the User object
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User getById(final Long id);
    User getByUsername(final String username);
}
