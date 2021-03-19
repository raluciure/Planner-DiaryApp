package ro.planner.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * This class implements a completed calendar entry that is used as an intermediate object between the model from the
 * data base and the model used in front-end.
 */
public class CompletedEntryDTO extends AbstractDTO {
    @NotNull
    private PersonDTO person;

    @NotNull
    private ActivityDTO activity;

    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateFrom;

    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTo;

    @NotNull
    @Min(0)
    @Max(5)
    private Double rating;

    @NotNull
    @Min(0)
    @Max(100)
    private Long completion;

    @NotNull
    private String finalNotes;

    public PersonDTO getPerson() {
        return person;
    }

    public void setPerson(PersonDTO person) {
        this.person = person;
    }

    public ActivityDTO getActivity() {
        return activity;
    }

    public void setActivity(ActivityDTO activity) {
        this.activity = activity;
    }

    public LocalDateTime getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDateTime dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDateTime getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDateTime dateTo) {
        this.dateTo = dateTo;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Long getCompletion() {
        return completion;
    }

    public void setCompletion(Long completion) {
        this.completion = completion;
    }

    public String getFinalNotes() {
        return finalNotes;
    }

    public void setFinalNotes(String finalNotes) {
        this.finalNotes = finalNotes;
    }
}
