package ro.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.planner.model.Category;

/**
 * This interface contains all the operations needed to manipulate data from the database for the Category object
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category getById(final Long id);
    Category getByName(final String name);
}
