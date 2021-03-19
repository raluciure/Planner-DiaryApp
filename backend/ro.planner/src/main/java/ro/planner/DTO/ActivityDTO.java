package ro.planner.DTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * This class implements an activity that is used as an intermediate object between the model from the data base and
 * the model used in front-end.
 */
public class ActivityDTO extends AbstractDTO {

    @NotBlank
    @NotNull
    private String name;

    @NotNull
    @NotBlank
    private String description;

    @NotNull
    private CategoryDTO category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }
}
