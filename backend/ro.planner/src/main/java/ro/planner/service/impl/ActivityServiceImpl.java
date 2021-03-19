package ro.planner.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.planner.model.Activity;
import ro.planner.model.CalendarEntry;
import ro.planner.model.CompletedEntry;
import ro.planner.repository.ActivityRepository;
import ro.planner.repository.CalendarEntryRepository;
import ro.planner.repository.CompletedEntryRepository;
import ro.planner.service.ActivityService;

import java.util.List;

/**
 * This class creates the logic of the application for the Activity model by using the methods from ActivityRepository
 */
@Service("activityService")
public class ActivityServiceImpl implements ActivityService {

    private ActivityRepository activityRepository;
    private CalendarEntryRepository calendarEntryRepository;
    private CompletedEntryRepository completedEntryRepository;

    @Autowired
    public ActivityServiceImpl(final ActivityRepository activityRepository,
                               final CalendarEntryRepository calendarEntryRepository,
                               final CompletedEntryRepository completedEntryRepository) {
        this.activityRepository = activityRepository;
        this.calendarEntryRepository = calendarEntryRepository;
        this.completedEntryRepository = completedEntryRepository;
    }

    /**
     * This method creates/updates an activity
     * @param activity
     */
    @Override
    @Transactional
    public void saveOrUpdate(final Activity activity) {
        getActivityRepository().save(activity);
    }

    /**
     * This method gets the activity that has the id = id
     * @param id
     * @return the activity
     */
    @Override
    @Transactional
    public Activity getById(final Long id) {
        return getActivityRepository().getById(id);
    }

    /**
     * This method gets the activity that has the name = name
     * @param name
     * @return the activity
     */
    @Override
    @Transactional
    public Activity getByName(final String name) {
        return getActivityRepository().getByName(name);
    }

    /**
     * This method gets all the activities from the data base
     * @return
     */
    @Override
    @Transactional
    public List<Activity> getAll() {
        return getActivityRepository().findAllByOrderByNameAsc();
    }

    /**
     * This method gets the activity that has personId= personId
     * @param personId
     * @return the activity
     */
    @Override
    @Transactional
    public List<Activity> getByPersonId(final Long personId) {
        return getActivityRepository().getByPersonId(personId);
    }

    /**
     * This method deletes the activity that has the id = id
     * @param id
     */
    @Override
    @Transactional
    public void delete(final Long id) {
        final List<CalendarEntry> calendarEntryList = getCalendarEntryRepository().getByActivityId(id);
        final List<CompletedEntry> completedEntryList = getCompletedEntryRepository().getByActivityId(id);
        if(calendarEntryList.isEmpty() && completedEntryList.isEmpty()) {
            getActivityRepository().deleteById(id);
        }
    }

    public ActivityRepository getActivityRepository() {
        return activityRepository;
    }

    public CalendarEntryRepository getCalendarEntryRepository() {
        return calendarEntryRepository;
    }

    public CompletedEntryRepository getCompletedEntryRepository() {
        return completedEntryRepository;
    }
}
