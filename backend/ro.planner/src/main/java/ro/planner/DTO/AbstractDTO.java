package ro.planner.DTO;

/**
 * This is the abstract class that contains the common attribute for all the classes : id
 */
public abstract class AbstractDTO {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
