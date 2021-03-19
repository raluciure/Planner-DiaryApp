package ro.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.planner.model.CompletedEntry;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This interface contains all the operations needed to manipulate data from the database for the CompletedEntry object
 */
public interface CompletedEntryRepository extends JpaRepository<CompletedEntry, Long> {
    List<CompletedEntry> getByPersonIdAndDateFromBetweenOrPersonIdAndDateToBetweenOrderByDateToAsc(
            final Long id1, final LocalDateTime date1, final LocalDateTime date2, final Long id2, final LocalDateTime date3, final LocalDateTime date4);
    List<CompletedEntry> getByActivityId(final Long id);
}
