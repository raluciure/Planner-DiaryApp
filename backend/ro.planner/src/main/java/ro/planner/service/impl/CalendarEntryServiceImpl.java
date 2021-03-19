package ro.planner.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.planner.model.CalendarEntry;
import ro.planner.model.CompletedEntry;
import ro.planner.repository.ActivityRepository;
import ro.planner.repository.CalendarEntryRepository;
import ro.planner.repository.CompletedEntryRepository;
import ro.planner.service.CalendarEntryService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class creates the logic of the application for the CalendarEntry model by using the methods from
 * CalendarEntryRepository
 */
@Service("calendarEntryService")
public class CalendarEntryServiceImpl implements CalendarEntryService {

    private CalendarEntryRepository calendarEntryRepository;
    private CompletedEntryRepository completedEntryRepository;
    private ActivityRepository activityRepository;

    @Autowired
    public CalendarEntryServiceImpl(final CalendarEntryRepository calendarEntryRepository,
                                    final CompletedEntryRepository completedEntryRepository,
                                    final ActivityRepository activityRepository) {
        this.calendarEntryRepository = calendarEntryRepository;
        this.completedEntryRepository = completedEntryRepository;
        this.activityRepository = activityRepository;
    }

    /**
     * This method creates/updates a calendar entry
     * @param calendarEntry
     */
    @Override
    @Transactional
    public void saveOrUpdate(final CalendarEntry calendarEntry) {
        getCalendarEntryRepository().save(calendarEntry);
    }

    /**
     * This method marks a calendar entry as a completed calendar entry
     * @param calendarEntry
     * @param partiallyCompletedEntry
     * @return a completed calendar entry
     */
    @Override
    @Transactional
    public CompletedEntry completeEntry(final CalendarEntry calendarEntry, final CompletedEntry partiallyCompletedEntry) {
        final CompletedEntry completedEntry = calendarEntryToCompletedEntry(calendarEntry, partiallyCompletedEntry);
        getCompletedEntryRepository().save(completedEntry);
        getCalendarEntryRepository().delete(calendarEntry);

        return completedEntry;
    }

    /**
     * This method gets the calendar entry that has the id = id
     * @param id
     * @return calendar entry
     */
    @Override
    @Transactional
    public CalendarEntry getById(final Long id) {
        return getCalendarEntryRepository().getById(id);
    }

    /**
     * This method gets all the calendar entries of a person on a specific day
     * @param personId
     * @param date
     * @return calendar entry list
     */
    @Override
    @Transactional
    public List<CalendarEntry> getActivitiesByPersonAndDay(final Long personId, final LocalDateTime date) {
        return getCalendarEntryRepository().getByPersonIdAndDateFromBetweenOrPersonIdAndDateToBetweenOrderByDateToAsc(
                personId, date, date.plusDays(1), personId, date, date.plusDays(1));
    }

    /**
     * This method deletes the calendar entry that has the id = id
     * @param id
     */
    @Override
    @Transactional
    public void delete(final Long id) {
        getCalendarEntryRepository().deleteById(id);
    }

    /**
     * This method sets the new attributes (rating, completion rate and final notes) after a calendar entry is marked
     * as completed
     * @param calendarEntry
     * @param partiallyCompletedEntry
     * @return the completed calendar entry
     */
    private CompletedEntry calendarEntryToCompletedEntry(final CalendarEntry calendarEntry, final CompletedEntry partiallyCompletedEntry) {
        final ModelMapper modelMapper = new ModelMapper();
        final CompletedEntry completedEntry = modelMapper.map(calendarEntry, CompletedEntry.class);
        completedEntry.setId(null);
        completedEntry.setRating(partiallyCompletedEntry.getRating());
        completedEntry.setCompletion(partiallyCompletedEntry.getCompletion());
        completedEntry.setFinalNotes(partiallyCompletedEntry.getFinalNotes());
        return completedEntry;
    }

    public CalendarEntryRepository getCalendarEntryRepository() {
        return calendarEntryRepository;
    }

    public CompletedEntryRepository getCompletedEntryRepository() {
        return completedEntryRepository;
    }

    public ActivityRepository getActivityRepository() {
        return activityRepository;
    }
}
