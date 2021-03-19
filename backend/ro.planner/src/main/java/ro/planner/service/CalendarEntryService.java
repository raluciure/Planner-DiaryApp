package ro.planner.service;

import ro.planner.model.CalendarEntry;
import ro.planner.model.CompletedEntry;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This interface contains the methods declared in CalendarEntryRepository
 */
public interface CalendarEntryService {
    void saveOrUpdate(final CalendarEntry calendarEntry);
    CompletedEntry completeEntry(final CalendarEntry calendarEntry, final CompletedEntry partiallyCompletedEntry);
    CalendarEntry getById(final Long id);
    List<CalendarEntry> getActivitiesByPersonAndDay(final Long personId, final LocalDateTime date);
    void delete(final Long id);
}
