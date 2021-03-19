package ro.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.planner.model.CalendarEntry;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This interface contains all the operations needed to manipulate data from the database for the CalendarEntry object
 */
public interface CalendarEntryRepository extends JpaRepository<CalendarEntry, Long> {
    CalendarEntry getById(final Long id);
    List<CalendarEntry> getByPersonIdAndDateFromBetweenOrPersonIdAndDateToBetweenOrderByDateToAsc(
            final Long id1, final LocalDateTime date1, final LocalDateTime date2, final Long id2, final LocalDateTime date3, final LocalDateTime date4);
    List<CalendarEntry> getByActivityId(final Long id);
}
