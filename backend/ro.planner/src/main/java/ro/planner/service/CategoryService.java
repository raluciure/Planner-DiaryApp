package ro.planner.service;

import ro.planner.model.Category;

import java.util.List;
/**
 * This interface contains the methods declared in CategoryRepository
 */
public interface CategoryService {
    void saveOrUpdate(final Category category);
    Category getById(final Long id);
    Category getByName(final String name);
    List<Category> getAll();
    void delete(final Long id);
}
