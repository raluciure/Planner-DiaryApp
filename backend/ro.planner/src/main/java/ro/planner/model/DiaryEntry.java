package ro.planner.model;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * This class contains the diary entry used in the data base and all its attributes
 */
@Entity
@Table(name = "diary_entry",
       uniqueConstraints = @UniqueConstraint(columnNames = {"date"}) )
public class DiaryEntry extends AbstractModel {

    @Column(nullable = false)
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    private Person person;

    @Column(nullable = false)
    private String notes;

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
