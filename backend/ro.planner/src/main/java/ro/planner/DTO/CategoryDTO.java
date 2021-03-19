package ro.planner.DTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * This class implements a category that is used as an intermediate object between the model from the data base and
 * the model used in front-end.
 */
public class CategoryDTO extends AbstractDTO {
    @NotNull
    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
