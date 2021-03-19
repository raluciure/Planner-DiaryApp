package ro.planner.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * This is the superclass, the abstract class that contains the common attribute for all the classes:id
 */
@MappedSuperclass
public abstract class AbstractModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
