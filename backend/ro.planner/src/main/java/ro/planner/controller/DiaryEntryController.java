package ro.planner.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.planner.DTO.DiaryEntryDTO;
import ro.planner.model.DiaryEntry;
import ro.planner.model.Person;
import ro.planner.service.DiaryEntryService;
import ro.planner.service.PersonService;

import javax.validation.Valid;

/**
 * This class gets the data from the DiaryEntry class from model package and implements operations that will be used
 * later in front-end.
 */
@RestController
@RequestMapping("diary-entry")
public class DiaryEntryController {
    private DiaryEntryService diaryEntryService;
    private PersonService personService;

    @Autowired
    public DiaryEntryController(final DiaryEntryService diaryEntryService,
                                final PersonService personService) {
        this.diaryEntryService = diaryEntryService;
        this.personService = personService;
    }

    /**
     * This method creates/updates a diary entry
     * @param diaryEntryDTO
     */
    @PostMapping
    @ResponseBody
    void saveOrUpdate(@RequestBody @Valid final DiaryEntryDTO diaryEntryDTO) {
        final ModelMapper modelMapper = new ModelMapper();
        final DiaryEntry diaryEntry = modelMapper.map(diaryEntryDTO, DiaryEntry.class);
        final Person person = getPersonService().getById(diaryEntry.getPerson().getId());
        diaryEntry.setPerson(person);
        getDiaryEntryService().saveOrUpdate(diaryEntry);
    }

    /**
     * This method gets the diary entry of a person on a specific day
     * @param diaryEntryDTO
     * @return the diary entry
     */
    @PutMapping("/person-day")
    @ResponseBody
    public DiaryEntryDTO getDiaryEntryByPersonAndDay(@RequestBody final DiaryEntryDTO diaryEntryDTO) {
        final ModelMapper modelMapper = new ModelMapper();
        final DiaryEntry diaryEntry = getDiaryEntryService().getByPersonAndDay(diaryEntryDTO.getPerson().getId(), diaryEntryDTO.getDate());
        if (diaryEntry == null) {
            return new DiaryEntryDTO();
        }
        else {
            return modelMapper.map(diaryEntry,DiaryEntryDTO.class);
        }
    }

    public DiaryEntryService getDiaryEntryService() {
        return diaryEntryService;
    }

    public PersonService getPersonService() {
        return personService;
    }
}
