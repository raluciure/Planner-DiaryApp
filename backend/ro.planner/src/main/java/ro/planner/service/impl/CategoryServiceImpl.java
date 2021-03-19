package ro.planner.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.planner.model.Activity;
import ro.planner.model.Category;
import ro.planner.repository.ActivityRepository;
import ro.planner.repository.CategoryRepository;
import ro.planner.service.CategoryService;

import java.util.List;

/**
 * This class creates the logic of the application for the Category model by using the methods from CategoryRepository
 */
@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    private ActivityRepository activityRepository;

    @Autowired
    public CategoryServiceImpl(final CategoryRepository categoryRepository,
                               final ActivityRepository activityRepository) {
        this.categoryRepository = categoryRepository;
        this.activityRepository = activityRepository;
    }

    /**
     * This method creates/updates a category
     * @param category
     */
    @Override
    @Transactional
    public void saveOrUpdate(final Category category) {
        getCategoryRepository().save(category);
    }

    /**
     * This method gets the category that has the id = id
     * @param id
     * @return category
     */
    @Override
    @Transactional
    public Category getById(final Long id) {
        return getCategoryRepository().getById(id);
    }

    /**
     * This method gets the category that has the name = name
     * @param name
     * @return category
     */
    @Override
    @Transactional
    public Category getByName(final String name) {
        return getCategoryRepository().getByName(name);
    }

    /**
     * This method gets all the categories from the data base.
     * @return list of all categories
     */
    @Override
    @Transactional
    public List<Category> getAll() {
        return getCategoryRepository().findAll();
    }

    /**
     * This method deletes the category that has the id = id
     * @param id
     */
    @Override
    @Transactional
    public void delete(final Long id) {
        List<Activity> activityList = getActivityRepository().getByCategoryId(id);
        if(activityList.isEmpty()) {
            getCategoryRepository().deleteById(id);
        }
    }

    public CategoryRepository getCategoryRepository() {
        return categoryRepository;
    }

    public ActivityRepository getActivityRepository() {
        return activityRepository;
    }
}
