package ro.planner.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.planner.model.DiaryEntry;
import ro.planner.repository.DiaryEntryRepository;
import ro.planner.service.DiaryEntryService;

import java.time.LocalDateTime;

/**
 * This class creates the logic of the application for the DiaryEntry model by using the methods from
 * DiaryEntryRepository
 */
@Service("diaryEntryService")
public class DiaryEntryServiceImpl implements DiaryEntryService {

    private DiaryEntryRepository diaryEntryRepository;

    @Autowired
    public DiaryEntryServiceImpl(final DiaryEntryRepository diaryEntryRepository) {
        this.diaryEntryRepository = diaryEntryRepository;
    }

    /**
     * This method creates/updates a diary entry
     * @param diaryEntry
     */
    @Override
    @Transactional
    public void saveOrUpdate(DiaryEntry diaryEntry) {
        if(diaryEntry.getId() != null) {
            final String newNotes = diaryEntry.getNotes();
            diaryEntry = getDiaryEntryRepository().getByPersonIdAndDateOrderById(diaryEntry.getPerson().getId(), diaryEntry.getDate());
            diaryEntry.setNotes(newNotes);
        }
        getDiaryEntryRepository().save(diaryEntry);
    }

    /**
     * This method gets the diary entry of a person on a specific day
     * @param personId
     * @param date
     * @return the diary entry
     */
    @Override
    @Transactional
    public DiaryEntry getByPersonAndDay(final Long personId, final LocalDateTime date) {
        return getDiaryEntryRepository().getByPersonIdAndDateOrderById(personId, date);
    }

    public DiaryEntryRepository getDiaryEntryRepository() {
        return diaryEntryRepository;
    }
}
