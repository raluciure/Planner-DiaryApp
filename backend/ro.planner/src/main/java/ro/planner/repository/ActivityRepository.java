package ro.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ro.planner.model.Activity;

import java.util.List;

/**
 * This interface contains all the operations needed to manipulate data from the database for the Activity object
 */
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Activity getById(final Long id);
    Activity getByName(final String name);

    @Query(nativeQuery = true,
    value = " SELECT activity.* " +
            " FROM activity JOIN completed_entry ON (activity.id = completed_entry.activity_id) " +
            " WHERE person_id = ?1 " +
            " UNION " +
            " SELECT activity.* " +
            " FROM activity JOIN calendar_entry ON (activity.id = calendar_entry.activity_id) " +
            " WHERE person_id = ?1 ")
    List<Activity> getByPersonId(final Long personId);
    List<Activity> getByCategoryId(final Long id);
    List<Activity> findAllByOrderByNameAsc();

}
