package ro.planner.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * This class contains the category used in the data base and all its attributes
 */
@Entity
@Table(name = "category")
public class Category extends AbstractModel {

    @Column(nullable = false)
    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
