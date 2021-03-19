package ro.planner.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.planner.DTO.CompletedEntryDTO;
import ro.planner.model.CompletedEntry;
import ro.planner.service.ActivityService;
import ro.planner.service.CompletedEntryService;
import ro.planner.service.PersonService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class gets the data from the CompletedEntry class from model package and implements operations that will be used
 * later in front-end.
 */
@RestController
@RequestMapping("/completed-entry")
public class CompletedEntryController {
    private CompletedEntryService completedEntryService;
    private PersonService personService;
    private ActivityService activityService;

    @Autowired
    public CompletedEntryController(final CompletedEntryService completedEntryService,
                                    final PersonService personService,
                                    final ActivityService activityService) {
        this.completedEntryService = completedEntryService;
        this.personService = personService;
        this.activityService = activityService;
    }

    /**
     * This method gets all the completed calendar entries of a person on a specific day
     * @param completedEntryDTO
     * @return the list of completed calendar entries
     */
    @PutMapping("/person-day")
    @ResponseBody
    public List<CompletedEntryDTO> getCompletedEntriesByPersonAndDay(@RequestBody final CompletedEntryDTO completedEntryDTO) {
        final ModelMapper modelMapper = new ModelMapper();
        final List<CompletedEntry> completedEntryList = getCompletedEntryService().
                getActivitiesByPersonAndDay(completedEntryDTO.getPerson().getId(),completedEntryDTO.getDateFrom());
        return completedEntryList.stream().
                map(completedEntry -> modelMapper.map(completedEntry,CompletedEntryDTO.class)).collect(Collectors.toList());
    }

    public CompletedEntryService getCompletedEntryService() {
        return completedEntryService;
    }

    public PersonService getPersonService() {
        return personService;
    }

    public ActivityService getActivityService() {
        return activityService;
    }
}
