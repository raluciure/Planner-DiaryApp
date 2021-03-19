package ro.planner.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.planner.DTO.CategoryDTO;
import ro.planner.model.Category;
import ro.planner.service.CategoryService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class gets the data from the Category class from model package and implements operations that will be used
 * later in front-end.
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    private CategoryService categoryService;

    @Autowired
    public CategoryController(final CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * This method creates/updates a category
     * @param categoryDTO
     */
    @PostMapping
    @ResponseBody
    public void saveOrUpdate(@RequestBody @Valid final CategoryDTO categoryDTO){
        final ModelMapper modelMapper = new ModelMapper();
        final Category category = modelMapper.map(categoryDTO, Category.class);
        getCategoryService().saveOrUpdate(category);
    }

    /**
     * This method gets all the categories from the data base
     * @return list of categories
     */
    @GetMapping
    @ResponseBody
    public List<CategoryDTO> getAll() {
        final ModelMapper modelMapper = new ModelMapper();
        final List<Category> categories = getCategoryService().getAll();
        return categories.stream().map(category -> modelMapper.map(category,CategoryDTO.class)).collect(Collectors.toList());
    }

    /**
     * This method deletes the category that has the id = id
     * @param id
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public void deleteById(@PathVariable final Long id) {
        getCategoryService().delete(id);
    }

    public CategoryService getCategoryService() {
        return categoryService;
    }
}
