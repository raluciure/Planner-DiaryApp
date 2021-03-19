package ro.planner.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
 * This class contains the activity used in the data base and all its attributes
 */
@Entity
@Table(name = "activity")
public class Activity extends AbstractModel {

    @Column(nullable = false)
    @NotBlank
    private String name;

    @Column(nullable = false)
    @NotBlank
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
