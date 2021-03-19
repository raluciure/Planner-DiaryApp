package ro.planner.DTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * This class implements a role that is used as an intermediate object between the model from the data base and
 * the model used in front-end.
 */
public class RoleDTO extends AbstractDTO {
    @NotNull
    @NotBlank
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
