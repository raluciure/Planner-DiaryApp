package ro.planner.service;

import ro.planner.model.CompletedEntry;

import java.time.LocalDateTime;
import java.util.List;
/**
 * This interface contains the methods declared in CompletedEntryRepository
 */
public interface CompletedEntryService {
    void saveOrUpdate(final CompletedEntry completedEntry);
    List<CompletedEntry> getActivitiesByPersonAndDay(final Long personId, final LocalDateTime date);
}
