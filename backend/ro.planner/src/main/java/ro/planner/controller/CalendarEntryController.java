package ro.planner.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.planner.DTO.CalendarEntryDTO;
import ro.planner.DTO.CompletedEntryDTO;
import ro.planner.model.Activity;
import ro.planner.model.CalendarEntry;
import ro.planner.model.CompletedEntry;
import ro.planner.model.Person;
import ro.planner.service.ActivityService;
import ro.planner.service.CalendarEntryService;
import ro.planner.service.PersonService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
/**
 * This class gets the data from the CalendarEntry class from model package and implements operations that will be used
 * later in front-end.
 */
@RestController
@RequestMapping("/calendar-entry")
public class CalendarEntryController {
    private CalendarEntryService calendarEntryService;
    private PersonService personService;
    private ActivityService activityService;

    @Autowired
    public CalendarEntryController(final CalendarEntryService calendarEntryService,
                                   final PersonService personService,
                                   final ActivityService activityService) {
        this.calendarEntryService = calendarEntryService;
        this.personService = personService;
        this.activityService = activityService;
    }

    /**
     * This method creates/updates a calendar entry.
     * @param calendarEntryDTO
     */
    @PostMapping
    @ResponseBody
    public void saveOrUpdate(@RequestBody @Valid final CalendarEntryDTO calendarEntryDTO) {
        final ModelMapper modelMapper = new ModelMapper();
        final CalendarEntry calendarEntry = modelMapper.map(calendarEntryDTO, CalendarEntry.class);
        final Person person = getPersonService().getById(calendarEntry.getPerson().getId());
        final Activity activity = getActivityService().getByName(calendarEntry.getActivity().getName());
        calendarEntry.setPerson(person);
        calendarEntry.setActivity(activity);
        getCalendarEntryService().saveOrUpdate(calendarEntry);
    }

    /**
     * This method marks an activity as completed
     * @param completedEntryDTO
     * @return
     */
    @PostMapping("/complete-activity")
    @ResponseBody
    public CompletedEntryDTO completeActivity(@RequestBody final CompletedEntryDTO completedEntryDTO) {
        final ModelMapper modelMapper = new ModelMapper();
        final CalendarEntry calendarEntry = getCalendarEntryService().getById(completedEntryDTO.getId());
        final CompletedEntry partiallyCompletedEntry = modelMapper.map(completedEntryDTO,CompletedEntry.class);
        final CompletedEntry completedEntry = getCalendarEntryService().completeEntry(calendarEntry, partiallyCompletedEntry);
        return modelMapper.map(completedEntry,CompletedEntryDTO.class);
    }

    /**
     * This method gets all the calendar entries for a person and date
     * @param calendarEntryDTO
     * @return list of calendar entries
     */
    @PutMapping("/person-day")
    @ResponseBody
    public List<CalendarEntryDTO> getCalendarEntriesByPersonAndDay(@RequestBody final CalendarEntryDTO calendarEntryDTO) {
        final ModelMapper modelMapper = new ModelMapper();
        final List<CalendarEntry> calendarEntryList = getCalendarEntryService().
                getActivitiesByPersonAndDay(calendarEntryDTO.getPerson().getId(),calendarEntryDTO.getDateFrom());
        return calendarEntryList.stream().
                map(calendarEntry -> modelMapper.map(calendarEntry,CalendarEntryDTO.class)).collect(Collectors.toList());
    }

    /**
     * Deletes the calendar entry that has the id = id
     * @param id
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public void deleteById(@PathVariable final Long id) {
        getCalendarEntryService().delete(id);
    }

    public CalendarEntryService getCalendarEntryService() {
        return calendarEntryService;
    }

    public PersonService getPersonService() {
        return personService;
    }

    public ActivityService getActivityService() {
        return activityService;
    }
}
