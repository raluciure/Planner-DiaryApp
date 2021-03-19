package ro.planner.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.planner.DTO.ActivityDTO;
import ro.planner.model.Activity;
import ro.planner.model.Category;
import ro.planner.service.ActivityService;
import ro.planner.service.CategoryService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class gets the data from the Activity class from model package and implements operations that will be used
 * later in front-end.
 */

@RestController
@RequestMapping("/activity")
public class ActivityController {
    private ActivityService activityService;
    private CategoryService categoryService;

    @Autowired
    public ActivityController(final ActivityService activityService,
                              final CategoryService categoryService) {
        this.activityService = activityService;
        this.categoryService = categoryService;
    }

    /**
     * This method creates/updates an activity.
     * @param activityDTO
     */
    @PostMapping
    @ResponseBody
    public void saveOrUpdate(@RequestBody @Valid final ActivityDTO activityDTO) {
        final ModelMapper modelMapper = new ModelMapper();
        final Activity activity = modelMapper.map(activityDTO, Activity.class);
        final Category category = getCategoryService().getByName(activity.getCategory().getName());
        activity.setCategory(category);
        getActivityService().saveOrUpdate(activity);
    }

    /**
     * This method gets all the activities from the database.
     * @return list of activities
     */
    @GetMapping
    @ResponseBody
    public List<ActivityDTO> getAll() {
        final ModelMapper modelMapper = new ModelMapper();
        final List<Activity> activities = getActivityService().getAll();
        return activities.stream().map(activity -> modelMapper.map(activity,ActivityDTO.class)).collect(Collectors.toList());
    }

    /**
     * This method gets an activity by personID
     * @param personId
     * @return the activity that has person's id = personId
     */
    @GetMapping("/person/{personId}")
    @ResponseBody
    public List<ActivityDTO> getByPersonId(@PathVariable final Long personId) {
        final ModelMapper modelMapper = new ModelMapper();
        final List<Activity> activities = getActivityService().getByPersonId(personId);
        return activities.stream().map(activity -> modelMapper.map(activity,ActivityDTO.class)).collect(Collectors.toList());
    }

    /**
     * This method deletes the activity that has the id = id
     * @param id
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public void deleteById(@PathVariable final Long id) {
        getActivityService().delete(id);
    }

    public ActivityService getActivityService() {
        return activityService;
    }

    public CategoryService getCategoryService() {
        return categoryService;
    }
}
