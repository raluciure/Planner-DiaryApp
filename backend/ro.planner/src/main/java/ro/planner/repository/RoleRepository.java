package ro.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.planner.model.Role;

/**
 * This interface contains all the operations needed to manipulate data from the database for the Role object
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role getById(final Long id);
}
