package ro.planner.service;

import ro.planner.model.Activity;

import java.util.List;

/**
 * This interface contains the methods declared in ActivityRepository
 */
public interface ActivityService {
    void saveOrUpdate(final Activity activity);
    Activity getById(final Long id);
    Activity getByName(final String name);
    List<Activity> getAll();
    List<Activity> getByPersonId(final Long personId);
    void delete(final Long id);
}
