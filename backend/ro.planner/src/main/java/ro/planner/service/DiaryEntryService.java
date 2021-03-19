package ro.planner.service;

import ro.planner.model.DiaryEntry;

import java.time.LocalDateTime;

/**
 * This interface contains the methods declared in DiaryEntryRepository
 */
public interface DiaryEntryService {
    void saveOrUpdate(final DiaryEntry diaryEntry);
    DiaryEntry getByPersonAndDay(final Long personId, final LocalDateTime date);
}
