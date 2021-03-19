package ro.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.planner.model.DiaryEntry;

import java.time.LocalDateTime;

/**
 * This interface contains all the operations needed to manipulate data from the database for the DiaryEntry object
 */
public interface DiaryEntryRepository extends JpaRepository<DiaryEntry, Long> {
    DiaryEntry getByPersonIdAndDateOrderById(final Long personId, final LocalDateTime date);
}
