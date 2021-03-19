package ro.planner.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.planner.model.CompletedEntry;
import ro.planner.repository.CompletedEntryRepository;
import ro.planner.service.CompletedEntryService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This class creates the logic of the application for the CompletedEntry model by using the methods from
 * CompletedEntryRepository
 */
@Service("completedEntryService")
public class CompletedEntryServiceImpl implements CompletedEntryService {

    private CompletedEntryRepository completedEntryRepository;

    @Autowired
    public CompletedEntryServiceImpl(final CompletedEntryRepository completedEntryRepository) {
        this.completedEntryRepository = completedEntryRepository;
    }

    /**
     * This method creates/updates a completed calendar entry
     * @param completedEntry
     */
    @Override
    @Transactional
    public void saveOrUpdate(final CompletedEntry completedEntry) {
        getCompletedEntryRepository().save(completedEntry);
    }

    /**
     * This method gets all the completed calendar entries of a person on a specific day
     * @param personId
     * @param date
     * @return the list of completed calendar entries
     */
    @Override
    @Transactional
    public List<CompletedEntry> getActivitiesByPersonAndDay(Long personId, LocalDateTime date) {
        return getCompletedEntryRepository().getByPersonIdAndDateFromBetweenOrPersonIdAndDateToBetweenOrderByDateToAsc(
                personId, date, date.plusDays(1), personId, date, date.plusDays(1));
    }

    public CompletedEntryRepository getCompletedEntryRepository() {
        return completedEntryRepository;
    }
}
